package cloud.zfwproject.abaapi.service.service.impl;

import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.service.mapper.UserMapper;
import cloud.zfwproject.abaapi.service.model.dto.UserRegisterDTO;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.service.UserService;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    @Override
    public User getUserByUserAccount(String userAccount) {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(User::getUserAccount, userAccount)
                .one();
    }

}




