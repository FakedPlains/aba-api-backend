package cloud.zfwproject.abaapi.service.service.impl;

import cloud.zfwproject.abaapi.common.constant.CommonConstant;
import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.service.mapper.UserInterfaceInfoMapper;
import cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo.UserInterfaceInfoAddDTO;
import cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo.UserInterfaceInfoQueryDTO;
import cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateDTO;
import cloud.zfwproject.abaapi.service.model.po.UserInterfaceInfo;
import cloud.zfwproject.abaapi.service.service.UserInterfaceInfoService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author 46029
 * @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Service实现
 * @createDate 2023-03-12 21:18:58
 */
@Validated
@Service("userInterfaceInfoService")
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Override
    public long addUserInterfaceInfo(@Validated UserInterfaceInfoAddDTO userInterfaceInfoAddDTO) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddDTO, userInterfaceInfo);
        // TODO  校验
//        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);
//        User loginUser = userService.getLoginUser(request);
//        userInterfaceInfo.setUserId(loginUser.getId());
        boolean result = this.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
        return userInterfaceInfo.getId();
    }

    @Override
    public boolean deleteUserInterfaceInfo(Long id) {
        if (id <= 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS);
        }
//        User user = userService.getLoginUser(request);
//        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = this.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        // TODO 仅本人或管理员可删除
//        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        return this.removeById(id);
    }

    @Override
    public boolean updateUserInterfaceInfo(@Validated UserInterfaceInfoUpdateDTO userInterfaceInfoUpdateDTO) {
        if (userInterfaceInfoUpdateDTO.getId() <= 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateDTO, userInterfaceInfo);
        // 参数校验
//        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
//        User user = userService.getLoginUser(request);
        long id = userInterfaceInfoUpdateDTO.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = this.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        // TODO 仅本人或管理员可修改
//        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        return this.updateById(userInterfaceInfo);
    }

    @Override
    public UserInterfaceInfo getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS);
        }
        return this.getById(id);
    }

    @Override
    public List<UserInterfaceInfo> listUserInterfaceInfo(@Validated UserInterfaceInfoQueryDTO userInterfaceInfoQueryDTO) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        if (userInterfaceInfoQueryDTO != null) {
            BeanUtils.copyProperties(userInterfaceInfoQueryDTO, userInterfaceInfo);
        }
        return this.lambdaQuery()
                .setEntity(userInterfaceInfo)
                .list();

    }

    @Override
    public Page<UserInterfaceInfo> listUserInterfaceInfoByPage(@Validated UserInterfaceInfoQueryDTO userInterfaceInfoQueryDTO) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryDTO, userInterfaceInfo);
        long current = userInterfaceInfoQueryDTO.getCurrent();
        long size = userInterfaceInfoQueryDTO.getPageSize();
        String sortField = userInterfaceInfoQueryDTO.getSortField();
        String sortOrder = userInterfaceInfoQueryDTO.getSortOrder();

        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfo);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        return this.page(new Page<>(current, size), queryWrapper);
    }

    /**
     * 修改接口调用次数
     *
     * @param userId          用户 id
     * @param interfaceInfoId 接口 id
     */
    @Override
    public void modifyInvokeCount(Long userId, Long interfaceInfoId) {
        boolean res = this.lambdaUpdate()
                .eq(UserInterfaceInfo::getUserId, userId)
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .setSql("total_num = total_num + 1")
                .setSql("left_num = left_num - 1")
                .gt(UserInterfaceInfo::getLeftNum, 0)
                .update();
        if (!res) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
    }

    /**
     * 获取接口剩余调用次数
     *
     * @param userId          用户 id
     * @param interfaceInfoId 接口 id
     * @return 剩余调用次数
     */
    @Override
    public int getInvokeLeftCount(Long userId, Long interfaceInfoId) {
        UserInterfaceInfo userInterfaceInfo = this.lambdaQuery()
                .eq(UserInterfaceInfo::getUserId, userId)
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .select(UserInterfaceInfo::getLeftNum)
                .one();
        if (userInterfaceInfo == null) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        return userInterfaceInfo.getLeftNum();
    }
}




