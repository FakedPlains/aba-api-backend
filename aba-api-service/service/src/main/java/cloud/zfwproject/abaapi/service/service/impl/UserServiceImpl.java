package cloud.zfwproject.abaapi.service.service.impl;

import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.common.service.RedisService;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import cloud.zfwproject.abaapi.service.encoder.PasswordEncoder;
import cloud.zfwproject.abaapi.service.mapper.UserMapper;
import cloud.zfwproject.abaapi.service.model.dto.DeleteDTO;
import cloud.zfwproject.abaapi.service.model.dto.user.*;
import cloud.zfwproject.abaapi.service.model.enums.UserEnum;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.model.vo.UserVO;
import cloud.zfwproject.abaapi.service.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cloud.zfwproject.abaapi.common.constant.RedisConstants.LOGIN_USER_TTL;
import static cloud.zfwproject.abaapi.common.constant.RedisConstants.USER_LOGIN_KEY_PREFIX;

/**
 * @author 46029
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-03-12 13:44:10
 */
@Validated
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private RedisService redisService;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录请求数据
     * @return 认证 token
     */
    @Override
    public String login(UserLoginDTO userLoginDTO) {
        /*if (!StrUtil.isBlank(oldToken)) {
            return oldToken;
        }*/
        // 1.根据邮箱查询用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_account", userLoginDTO.getUserAccount());
        User user = baseMapper.selectOne(wrapper);
        // 2.判断用户是否存在
        if (user == null)
            throw new BusinessException("用户不存在");
        // 3.判断密码是否正确
        String password = userLoginDTO.getUserPassword();
        String md5 = passwordEncoder.encode(password);
        if (!md5.equals(user.getUserPassword()))
            throw new BusinessException("密码错误");
        // 4.随机生成 token 作为登录令牌
        String token = UUID.randomUUID().toString(true);
        // 5.保存用户到 redis
        String tokenKey = USER_LOGIN_KEY_PREFIX + token;
        SimpleUser simpleUser = BeanUtil.copyProperties(user, SimpleUser.class);
        redisService.setWithString(tokenKey, simpleUser, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return token;
    }

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
            Long count = this.lambdaQuery()
                    .eq(User::getUserAccount, userAccount)
                    .count();
            if (count > 0) {
                throw new BusinessException(ResponseCode.INVALID_PARAMS, "账号重复");
            }
            // 3.密码加密、生成用户名、全局唯一 Id 生成
            String md5 = passwordEncoder.encode(userPassword);
            long id = IdUtil.getSnowflakeNextId();
            // 4. 生成 accessKey、secretKey
            String accessKey = DigestUtil.md5Hex(userAccount + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(userAccount + RandomUtil.randomNumbers(8));
            // 5.插入数据
            User user = new User();
            user.setId(id);
            user.setUserAccount(userAccount);
            user.setUserPassword(md5);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            boolean save = this.save(user);
            if (!save) {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    /**
     * 退出登录
     *
     * @return 是否成功
     */
    @Override
    public boolean logout(String token) {
        if (StrUtil.isBlank(token)) {
            throw new BusinessException("未登录");
        }
        String tokenKey = USER_LOGIN_KEY_PREFIX + token;
        redisService.delete(tokenKey);
        UserHolder.removeUser();
        return true;
    }

    /**
     * 根据用户账号获取用户
     *
     * @param userAccount 用户账号
     * @return 用户
     */
    @Override
    public User getUserByUserAccount(String userAccount) {
        return this.lambdaQuery()
                .eq(User::getUserAccount, userAccount)
                .one();
    }

    /**
     * 分页获取用户数据
     *
     * @param userQueryDTO 用户查询分页庆请求
     * @return 分页数据
     */
    @Override
    public Page<UserVO> getUserPage(@Validated UserQueryDTO userQueryDTO) {
        User userQuery = new User();
        BeanUtil.copyProperties(userQueryDTO, userQuery);
        Page<User> userPage = this.lambdaQuery()
                .like(StrUtil.isNotBlank(userQuery.getUserName()), User::getUserName, userQuery.getUserName())
                .like(StrUtil.isNotBlank(userQuery.getUserAccount()), User::getUserAccount, userQuery.getUserAccount())
                .eq(StrUtil.isNotBlank(userQuery.getUserRole()), User::getUserRole, userQuery.getUserRole())
                .page(new Page<>(userQueryDTO.getCurrent(), userQueryDTO.getPageSize()));
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

    /**
     * 添加用户
     *
     * @param userAddDTO 添加用户数据
     * @return 用户 id
     */
    @Override
    public Long addUser(@Validated UserAddDTO userAddDTO) {
        User user = new User();
        BeanUtil.copyProperties(userAddDTO, user);
        user.setUserPassword("111111");
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "添加失败，数据库错误");
        }
        return user.getId();
    }

    /**
     * 更新用户
     *
     * @param userUpdateDTO 更新用户数据
     * @return 是否成功
     */
    @Override
    public Boolean updateUser(@Validated UserUpdateDTO userUpdateDTO) {
        User user = new User();
        BeanUtil.copyProperties(userUpdateDTO, user);
        return this.updateById(user);
    }

    @Override
    public Boolean deleteUser(@Validated DeleteDTO deleteDTO) {
        return this.removeById(deleteDTO.getId());
    }

    /**
     * 根据 accessKey 获取用户信息
     *
     * @param accessKey accessKey
     * @return 用户信息
     */
    @Override
    public User getUserByAccessKey(String accessKey) {
        return this.lambdaQuery()
                .eq(StrUtil.isNotBlank(accessKey), User::getAccessKey, accessKey)
                .select(User::getId, User::getAccessKey, User::getSecretKey)
                .one();
    }

    /**
     * 根据用 id 获取 secretKey
     *
     * @param id 用户 id
     * @return secretKey
     */
    @Override
    public String getSecretKeyByUserId(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS);
        }
        User user = this.lambdaQuery()
                .eq(User::getId, id)
                .select(User::getSecretKey)
                .one();
        if (user == null) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "用户不存在");
        }
        return user.getSecretKey();
    }

    /**
     * 修改密码
     *
     * @param updatePasswordRequest 修改密码请求对象
     * @return 是否成功
     */
    @Override
    public Boolean updatePassword(@Validated UserUpdatePasswordRequest updatePasswordRequest) {
        // 1.判断两次密码是否一致
        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getRepeatPassword())) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "两次输入的密码不一致");
        }

        // 2.判断旧密码是否相同
        User user = this.lambdaQuery()
                .eq(User::getId, updatePasswordRequest.getId())
                .select(User::getUserPassword)
                .one();
        if (user == null) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "用户不存在");
        }
        String oldPassword = updatePasswordRequest.getOldPassword();
        String md5 = SecureUtil.md5(oldPassword);
        if (!user.getUserPassword().equals(md5)) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "密码错误");
        }

        // 3.更新密码
        String newPassword = updatePasswordRequest.getNewPassword();
        md5 = SecureUtil.md5(newPassword);
        user.setUserPassword(md5);
        boolean res = this.updateById(user);
        if (!res) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "修改失败");
        }

        return true;
    }

    /**
     * 判断当前登录用户是否有管理员权限
     *
     * @return 是否有权限
     */
    @Override
    public Boolean isAdmin() {
        SimpleUser user = UserHolder.getUser();
        return UserEnum.Role.ADMIN.getText().equals(user.getUserRole());
    }

}




