package cloud.zfwproject.abaapi.service.service.impl;

import cloud.zfwproject.abaapi.common.constant.CommonConstant;
import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.service.mapper.InterfaceInfoMapper;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoAddDTO;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoQueryDTO;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoUpdateDTO;
import cloud.zfwproject.abaapi.service.model.enums.InterfaceInfoStatusEnum;
import cloud.zfwproject.abaapi.service.model.po.InterfaceInfo;
import cloud.zfwproject.abaapi.service.service.InterfaceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author 46029
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2023-03-12 21:18:55
 */
@Validated
@Service("interfaceInfoService")
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

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
    public Long addInterfaceInfo(@Validated InterfaceInfoAddDTO interfaceInfoAddDTO) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddDTO, interfaceInfo);
        // TODO 添加创建用户 校验
//        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(1L);
        boolean result = this.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return interfaceInfo.getId();
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
    public InterfaceInfo getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS);
        }
        return this.getById(id);
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
        return updateInterfaceInfoStatus(id, InterfaceInfoStatusEnum.ONLINE.getValue());
    }

    @Override
    public boolean offlineInterfaceInfo(Long id) {
        return updateInterfaceInfoStatus(id, InterfaceInfoStatusEnum.OFFLINE.getValue());
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




