package cloud.zfwproject.abaapi.service.service.impl;

import cloud.zfwproject.abaapi.common.constant.CommonConstant;
import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import cloud.zfwproject.abaapi.service.mapper.InterfaceInfoMapper;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.*;
import cloud.zfwproject.abaapi.service.model.enums.InterfaceInfoEnum;
import cloud.zfwproject.abaapi.service.model.po.InterfaceInfo;
import cloud.zfwproject.abaapi.service.model.po.InterfaceParam;
import cloud.zfwproject.abaapi.service.model.vo.InterfaceInfoVO;
import cloud.zfwproject.abaapi.service.service.InterfaceInfoService;
import cloud.zfwproject.abaapi.service.service.InterfaceParamService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
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
    private InterfaceParamService interfaceParamService;

    @Override
    public Page<InterfaceInfo> getInterfaceInfoPages(@Validated InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryDTO, interfaceInfoQuery);
        long current = interfaceInfoQueryDTO.getCurrent();
        long size = interfaceInfoQueryDTO.getPageSize();
        String sortField = interfaceInfoQueryDTO.getSortField();
        String sortOrder = interfaceInfoQueryDTO.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // content 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "content", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return this.page(new Page<>(current, size), queryWrapper);
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

    @Override
    public boolean deleteInterfaceInfo(Long id) {
//        User user = userService.getLoginUser(request);
//        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        // TODO 仅本人或管理员可删除
//        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ResponseCode.PERMISSION_DENIED);
//        }
        return this.removeById(id);
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

    @Override
    public InterfaceInfoVO getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS);
        }
        InterfaceInfo interfaceInfo = this.getById(id);
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtil.copyProperties(interfaceInfo, interfaceInfoVO);
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
    public boolean onlineInterfaceInfo(Long id) {
        return updateInterfaceInfoStatus(id, InterfaceInfoEnum.Status.ONLINE.getValue());
    }

    @Override
    public boolean offlineInterfaceInfo(Long id) {
        return updateInterfaceInfoStatus(id, InterfaceInfoEnum.Status.OFFLINE.getValue());
    }

    private boolean updateInterfaceInfoStatus(Long id, Integer status) {
        if (id <= 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS);
        }
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(status);
        return this.updateById(interfaceInfo);
    }

}




