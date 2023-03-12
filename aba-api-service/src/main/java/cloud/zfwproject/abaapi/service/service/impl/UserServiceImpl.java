package cloud.zfwproject.abaapi.service.service.impl;

import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.service.mapper.UserMapper;
import cloud.zfwproject.abaapi.service.model.dto.user.UserQueryRequest;
import cloud.zfwproject.abaapi.service.model.dto.user.UserRegisterDTO;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.model.vo.UserVO;
import cloud.zfwproject.abaapi.service.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 46029
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-03-12 13:44:10
 */
@Validated
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册数据
     * @return 用户 id
     */
    @Override
    public Long register(@Validated UserRegisterDTO userRegisterDTO) {
        // 1.判断两次密码是否一致
        if (!userRegisterDTO.getUserPassword().equals(userRegisterDTO.getRepeatPassword())) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "两次输入的密码不一致");
        }

        String userAccount = userRegisterDTO.getUserAccount();
        String userPassword = userRegisterDTO.getUserPassword();
        synchronized (userAccount.intern()) {
            // 2.判断账号是否重复
            Long count = new LambdaQueryChainWrapper<>(baseMapper)
                    .eq(User::getUserAccount, userAccount)
                    .count();
            if (count > 0) {
                throw new BusinessException(ResponseCode.INVALID_PARAMS, "账号重复");
            }
            //TODO 3.密码加密、生成用户名、全局唯一 Id 生成

            // 4.插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(userPassword);
            boolean save = this.save(user);
            if (!save) {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    /**
     * 根据用户账号获取用户
     *
     * @param userAccount 用户账号
     * @return 用户
     */
    @Override
    public User getUserByUserAccount(String userAccount) {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(User::getUserAccount, userAccount)
                .one();
    }

    /**
     * 分页获取用户数据
     *
     * @param userQueryRequest 用户查询分页庆请求
     * @return 分页数据
     */
    @Override
    public Page<UserVO> getUserPage(UserQueryRequest userQueryRequest) {
        User userQuery = new User();
        BeanUtil.copyProperties(userQueryRequest, userQuery);
        Page<User> userPage = new LambdaQueryChainWrapper<>(baseMapper)
                .setEntity(userQuery)
                .page(new Page<>(userQueryRequest.getCurrent(), userQueryRequest.getPageSize()));
        List<UserVO> userVOS = userPage.getRecords().stream()
                .map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtil.copyProperties(user, userVO);
                    return userVO;
                })
                .collect(Collectors.toList());
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        userVOPage.setRecords(userVOS);
        return userVOPage;
    }

}




