package cloud.zfwproject.abaapi.service.service.impl;

import cloud.zfwproject.abaapi.common.constant.RedisConstants;
import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.common.service.RedisService;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import cloud.zfwproject.abaapi.service.mapper.InterfaceInfoMapper;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.*;
import cloud.zfwproject.abaapi.service.model.enums.InterfaceInfoEnum;
import cloud.zfwproject.abaapi.service.model.po.*;
import cloud.zfwproject.abaapi.service.model.vo.InterfaceInfoVO;
import cloud.zfwproject.abaapi.service.model.vo.InterfaceInvokeVO;
import cloud.zfwproject.abaapi.service.service.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author 46029
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2023-03-12 21:18:55
 */
@Validated
@Service("interfaceInfoService")
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private Executor asyncTaskExecutor;

    @Resource
    private RedisService redisService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceParamService interfaceParamService;

    @Lazy
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private InterfaceChargingService interfaceChargingService;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 分页获取接口列表
     *
     * @param interfaceInfoQueryDTO 接口查询书籍
     * @return 接口分页列表
     */
    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoPages(@Validated InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryDTO, interfaceInfoQuery);
        long current = interfaceInfoQueryDTO.getCurrent();
        long size = interfaceInfoQueryDTO.getPageSize();
//        String sortField = interfaceInfoQueryDTO.getSortField();
//        String sortOrder = interfaceInfoQueryDTO.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        String name = interfaceInfoQuery.getName();
        Integer status = interfaceInfoQuery.getStatus();

        Page<InterfaceInfo> interfaceInfoPage = this.lambdaQuery()
                .eq(!userService.isAdmin(), InterfaceInfo::getUserId, interfaceInfoQuery.getUserId())
                .like(StringUtils.isNotBlank(name), InterfaceInfo::getName, name)
                .like(StringUtils.isNotBlank(description), InterfaceInfo::getDescription, description)
                .eq(status != null, InterfaceInfo::getStatus, status)
//                .orderBy(StrUtil.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC))
                .page(new Page<>(current, size));

        List<InterfaceInfoVO> interfaceInfoVOS = interfaceInfoPage.getRecords()
                .stream().parallel()
                .map(interfaceInfo -> {
                    InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
                    BeanUtil.copyProperties(interfaceInfo, interfaceInfoVO);
                    // 获取创建人
                    User user = userService.getById(interfaceInfoVO.getUserId());
                    interfaceInfoVO.setUserAccount(user.getUserAccount());
                    // 获取总调用次数
                    long count = userInterfaceInfoService.getInvokeCountByInterfaceId(interfaceInfo.getId());
                    interfaceInfoVO.setTotalInvokeCount(count);

                    return interfaceInfoVO;
                })
                .collect(Collectors.toList());

        Page<InterfaceInfoVO> interfaceInfoVOPage = new PageDTO<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        interfaceInfoVOPage.setRecords(interfaceInfoVOS);
        return interfaceInfoVOPage;
    }

    /**
     * 创建
     *
     * @param interfaceInfoAddDTO 接口创建数据
     * @return 返回接口 id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addInterfaceInfo(@Validated InterfaceInfoAddRequest interfaceInfoAddDTO) {
        // 1.添加接口信息
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddDTO, interfaceInfo);
        Long userId = UserHolder.getUser().getId();
        interfaceInfo.setUserId(userId);
        String dataId = SecureUtil.md5(interfaceInfo.getMethod() + ":" + interfaceInfo.getUrl());
        interfaceInfo.setDataId(dataId);
        boolean result = this.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        // 2.添加接口参数信息
        Long interfaceInfoId = interfaceInfo.getId();
        List<RequestHeader> requestHeaders = interfaceInfoAddDTO.getRequestHeaders();
        List<RequestParam> requestParams = interfaceInfoAddDTO.getRequestParams();
        List<ResponseParam> responseParams = interfaceInfoAddDTO.getResponseParams();
        List<ErrorCode> errorCode = interfaceInfoAddDTO.getErrorCode();

        List<InterfaceParam> collect1 = requestHeaders.stream().map(requestHeader -> {
            InterfaceParam interfaceParam = new InterfaceParam();
            BeanUtil.copyProperties(requestHeader, interfaceParam);
            interfaceParam.setInterfaceInfoId(interfaceInfoId);
            interfaceParam.setStyle(InterfaceInfoEnum.Style.HEADER.getValue());
            return interfaceParam;
        }).collect(Collectors.toList());

        List<InterfaceParam> collect2 = requestParams.stream().map(requestHeader -> {
            InterfaceParam interfaceParam = new InterfaceParam();
            BeanUtil.copyProperties(requestHeader, interfaceParam);
            interfaceParam.setInterfaceInfoId(interfaceInfoId);
            return interfaceParam;
        }).collect(Collectors.toList());

        List<InterfaceParam> collect3 = responseParams.stream().map(requestHeader -> {
            InterfaceParam interfaceParam = new InterfaceParam();
            BeanUtil.copyProperties(requestHeader, interfaceParam);
            interfaceParam.setInterfaceInfoId(interfaceInfoId);
            interfaceParam.setStyle(InterfaceInfoEnum.Style.RETURN.getValue());
            return interfaceParam;
        }).collect(Collectors.toList());

        List<InterfaceParam> collect4 = errorCode.stream().map(requestHeader -> {
            InterfaceParam interfaceParam = new InterfaceParam();
            BeanUtil.copyProperties(requestHeader, interfaceParam);
            interfaceParam.setInterfaceInfoId(interfaceInfoId);
            interfaceParam.setStyle(InterfaceInfoEnum.Style.ERROR.getValue());
            return interfaceParam;
        }).collect(Collectors.toList());

        List<InterfaceParam> interfaceParams = CollUtil.unionAll(collect1, collect2, collect3, collect4);
        result = interfaceParamService.saveBatch(interfaceParams);
        if (!result) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }

        return interfaceInfoId;
    }

    /**
     * 删除
     *
     * @param id 接口 id
     */
    @Override
    public void deleteInterfaceInfo(Long id) {
        SimpleUser user = UserHolder.getUser();
        Long userId = user.getId();
        // 判断是否存在
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!interfaceInfo.getUserId().equals(userId) && !userService.isAdmin()) {
            throw new BusinessException(ResponseCode.PERMISSION_DENIED);
        }
        boolean res = this.removeById(id);
        if (!res) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "删除失败");
        }
        // TODO 异步删除接口相关信息
        asyncTaskExecutor.execute(() ->
                interfaceParamService.deleteInterfaceParamsByInterfaceId(id)
        );

        // 3.删除 redis 数据
        redisService.deleteHashKey(RedisConstants.INTERFACE_INFO_PREFIX, interfaceInfo.getDataId());
    }

    @Override
    public boolean updateInterfaceInfo(@Validated InterfaceInfoUpdateDTO interfaceInfoUpdateDTO) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateDTO, interfaceInfo);
        // 参数校验
//        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateDTO.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        // TODO 仅本人或管理员可修改
//        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        return this.updateById(interfaceInfo);
    }

    /**
     * 根据 id 获取
     *
     * @param id 接口 id
     * @return 接口信息
     */
    @Override
    public InterfaceInfoVO getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS);
        }
        // 1.获取接口信息
        InterfaceInfo interfaceInfo = this.getById(id);
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtil.copyProperties(interfaceInfo, interfaceInfoVO);

        CompletableFuture<Void> userInfoTask = CompletableFuture.runAsync(() -> {
            // 2.获取用户信息
            User user = userService.getById(interfaceInfoVO.getUserId());
            interfaceInfoVO.setUserAccount(user.getUserAccount());
        }, asyncTaskExecutor);

        CompletableFuture<Void> interfaceParamsTask = CompletableFuture.runAsync(() -> {
            // 3.获取接口参数信息
            List<InterfaceParam> interfaceParams = interfaceParamService.getInterfaceParamsByInterfaceId(id);
            Map<Integer, List<InterfaceParam>> paramsMap = interfaceParams.stream()
                    .collect(Collectors.groupingBy(InterfaceParam::getStyle));

            List<InterfaceParam> requestParams = CollUtil.unionAll(paramsMap.get(InterfaceInfoEnum.Style.PATH.getValue()), paramsMap.get(InterfaceInfoEnum.Style.QUERY.getValue()), paramsMap.get(InterfaceInfoEnum.Style.BODY.getValue()));
            interfaceInfoVO.setRequestParams(requestParams);
            List<InterfaceParam> requestHeaders = paramsMap.get(InterfaceInfoEnum.Style.HEADER.getValue());
            interfaceInfoVO.setRequestHeaders(requestHeaders);
            List<InterfaceParam> responseParams = paramsMap.get(InterfaceInfoEnum.Style.RETURN.getValue());
            interfaceInfoVO.setResponseParams(responseParams);
            List<InterfaceParam> errorCode = paramsMap.get(InterfaceInfoEnum.Style.ERROR.getValue());
            interfaceInfoVO.setErrorCode(errorCode);
        }, asyncTaskExecutor);

        CompletableFuture<Void> interfaceChargingTask = CompletableFuture.runAsync(() -> {
            // 4.获取接口计费信息
            InterfaceCharging interfaceCharging = interfaceChargingService.getInterfaceChargingByInterfaceId(id);
            interfaceInfoVO.setInterfaceCharging(interfaceCharging);
        }, asyncTaskExecutor);

        CompletableFuture<Void> hasFreeTask = CompletableFuture.runAsync(() -> {
            // 5.获取用户是否有免费调用次数
            UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getByUserIdAndInterfaceId(interfaceInfo.getUserId(), id);
            interfaceInfoVO.setHasFree(userInterfaceInfo != null && userInterfaceInfo.getHasFree() == 1 ? 1 : 0);
        }, asyncTaskExecutor);

        CompletableFuture<Void> tasks = CompletableFuture.allOf(userInfoTask, interfaceParamsTask, interfaceChargingTask, hasFreeTask);
        tasks.join();

        return interfaceInfoVO;
    }

    @Override
    public List<InterfaceInfo> listInterfaceInfo(@Validated InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryDTO != null) {
            BeanUtils.copyProperties(interfaceInfoQueryDTO, interfaceInfoQuery);
        }
        return this.lambdaQuery()
                .setEntity(interfaceInfoQuery)
                .list();
    }

    @Override
    public InterfaceInfo getInterfaceInfoByDataId(String dataId) {
        return this.lambdaQuery()
                .eq(StrUtil.isNotBlank(dataId), InterfaceInfo::getDataId, dataId)
                .one();
    }

    /**
     * 发布
     *
     * @param id 接口 id
     * @return 是否成功
     */
    @Override
    public boolean onlineInterfaceInfo(Long id) {
        // 1.判断是否存在
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        // 2.接口计费信息是否设置
        InterfaceCharging interfaceCharging = interfaceChargingService.getInterfaceChargingByInterfaceId(id);
        if (interfaceCharging == null) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "请先设置计费信息");
        }

        // 2.审核接口是否可用
        kafkaTemplate.send("", "");

        // 3.更新接口状态
        boolean res = updateInterfaceInfoStatus(id, InterfaceInfoEnum.Status.ONLINE.getValue());
        if (!res) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "发布失败");
        }
        // 4.存储到 redis
        redisService.setWithHash(RedisConstants.INTERFACE_INFO_PREFIX, interfaceInfo.getDataId(), interfaceInfo.getUrl());
        return true;
    }

    /**
     * 下线
     *
     * @param id 接口 id
     * @return 是否成功
     */
    @Override
    public boolean offlineInterfaceInfo(Long id) {
        // 1.判断是否存在
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        // 2.修改状态
        boolean res = updateInterfaceInfoStatus(id, InterfaceInfoEnum.Status.OFFLINE.getValue());
        if (!res) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "下线失败");
        }
        // 3.删除 redis 数据
        redisService.deleteHashKey(RedisConstants.INTERFACE_INFO_PREFIX, interfaceInfo.getDataId());
        return true;
    }

    /**
     * 分页获取展示接口列表
     *
     * @param interfaceInfoQueryDTO 接口查询书籍
     * @return 接口分页列表
     */
    @Override
    public Page<InterfaceInfoVO> getShowingInterfaceInfo(InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
        interfaceInfoQueryDTO.setStatus(InterfaceInfoEnum.Status.ONLINE.getValue());
        return this.getInterfaceInfoPages(interfaceInfoQueryDTO);
    }

    /**
     * 测试调用
     *
     * @param invokeRequest 调用请求
     * @return 响应数据
     */
    @Override
    public InterfaceInvokeVO invokeInterface(InterfaceInfoInvokeDTO invokeRequest) {
        long id = invokeRequest.getId();
        // 判断是否存在
        InterfaceInfo interfaceInfo = this.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        // 判断接口状态
        if (interfaceInfo.getStatus() == InterfaceInfoEnum.Status.OFFLINE.getValue()) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "接口已关闭");
        }

        // TODO 使用 client 调用
        SimpleUser user = UserHolder.getUser();
        String accessKey = user.getAccessKey();

        String url = interfaceInfo.getUrl();
        Integer method = interfaceInfo.getMethod();

        Map<String, String> pathParams = invokeRequest.getPathParams();
        Map<String, String> headerParams = invokeRequest.getHeaderParams();
        Map<String, Object> queryParams = invokeRequest.getQueryParams();
        String bodyParams = invokeRequest.getBodyParams();

        for (String key : pathParams.keySet()) {
            url = url.replace("{" + key + "}", pathParams.get(key));
        }

        HttpRequest request = HttpRequest.of(url).method(getMethod(method));
        if (CollUtil.isNotEmpty(headerParams)) {
            request.addHeaders(headerParams);
        }
        if (CollUtil.isNotEmpty(queryParams)) {
            request.form(queryParams);
        }
        if (StrUtil.isNotBlank(bodyParams)) {
            request.body(bodyParams);
        }
        try (HttpResponse response = request.execute()) {
            String body = response.body();
            Map<String, List<String>> headers = response.headers();
            headers = headers.keySet().stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(k -> k, headers::get));

            InterfaceInvokeVO interfaceInvokeVO = new InterfaceInvokeVO();
            interfaceInvokeVO.setResponseBody(body);
            interfaceInvokeVO.setResponseHeaders(headers);
            return interfaceInvokeVO;
        }
    }

    private boolean updateInterfaceInfoStatus(Long id, Integer status) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(status);
        return this.updateById(interfaceInfo);
    }

    private Method getMethod(Integer value) {
        switch (value) {
            case 0:
                return Method.GET;
            case 1:
                return Method.POST;
            case 2:
                return Method.PUT;
            case 3:
                return Method.DELETE;
        }
        return null;
    }

}




