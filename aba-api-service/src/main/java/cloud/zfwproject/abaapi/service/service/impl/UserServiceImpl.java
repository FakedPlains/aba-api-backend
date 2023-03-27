package cloud.zfwproject.abaapi.service.service.impl;

import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.service.mapper.UserMapper;
import cloud.zfwproject.abaapi.service.model.dto.DeleteDTO;
import cloud.zfwproject.abaapi.service.model.dto.user.*;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.model.vo.UserVO;
import cloud.zfwproject.abaapi.service.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private StringRedisTemplate stringRedisTemplate;

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
        String md5 = SecureUtil.md5(password);
        if (!md5.equals(user.getUserPassword()))
            throw new BusinessException("密码错误");
        // 4.随机生成 token 作为登录令牌
        String token = UUID.randomUUID().toString(true);
        // 5.保存用户到 redis
        String tokenKey = USER_LOGIN_KEY_PREFIX + token;
        SimpleUser simpleUser = BeanUtil.copyProperties(user, SimpleUser.class);
        String str = JSONUtil.toJsonStr(simpleUser);
        stringRedisTemplate.opsForValue().set(tokenKey, str, LOGIN_USER_TTL, TimeUnit.MINUTES);
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
            //TODO 3.密码加密、生成用户名、全局唯一 Id 生成
            String md5 = SecureUtil.md5(userPassword);
            // 4.插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(md5);
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
        Page<User> userPage = new LambdaQueryChainWrapper<>(baseMapper)
                .setEntity(userQuery)
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

}




