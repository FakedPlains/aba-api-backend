package cloud.zfwproject.abaapi.service.controller;

import cloud.zfwproject.abaapi.common.constant.CommonConstant;
import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import cloud.zfwproject.abaapi.service.model.dto.DeleteDTO;
import cloud.zfwproject.abaapi.service.model.dto.user.*;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.model.vo.UserVO;
import cloud.zfwproject.abaapi.service.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/12 14:16
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册请求数据
     * @return 返回用户 id
     */
    @PostMapping("register")
    public ResponseResult<Long> register(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {
        Long userId = userService.register(userRegisterDTO);
        return ResponseUtils.success(userId);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录请求数据
     * @return 认证 token
     */
    @PostMapping("login")
    public ResponseResult<String> login(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO);
        return ResponseUtils.success(token);
    }

    /**
     * 退出登录
     *
     * @return 是否成功
     */
    @PostMapping("logout")
    public ResponseResult<Boolean> logout(HttpServletRequest request) {
        String token = request.getHeader(CommonConstant.REQUEST_HEADER_TOKEN);
        boolean res = userService.logout(token);
        return ResponseUtils.success(res);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @GetMapping("current")
    public ResponseResult<SimpleUser> current() {
        SimpleUser user = UserHolder.getUser();
        return ResponseUtils.success(user);
    }

    /**
     * 获取当前登录用户 secretKey
     *
     * @return secretKey
     */
    @PostMapping("secretKey")
    public ResponseResult<String> getSecretKey() {
        SimpleUser user = UserHolder.getUser();
        String secretKet = userService.getSecretKeyByUserId(user.getId());
        return ResponseUtils.success(secretKet);
    }

    /**
     * 根据用户账号获取用户信息
     *
     * @param userAccount 用户账号
     * @return 用户信息
     */
    @GetMapping("account/{userAccount}")
    public ResponseResult<User> getUserByUserAccount(@PathVariable("userAccount") String userAccount) {
        User user = userService.getUserByUserAccount(userAccount);
        return ResponseUtils.success(user);
    }

    /**
     * 根据 accessKey 获取用户信息
     *
     * @param accessKey accessKey
     * @return 用户信息
     */
    @GetMapping("access/{accessKey}")
    public ResponseResult<User> getUserByAccessKey(@PathVariable String accessKey) {
        User user = userService.getUserByAccessKey(accessKey);
        return ResponseUtils.success(user);
    }

    /**
     * 分页获取用户数据
     *
     * @param userQueryDTO 用户查询分页请求
     * @return 分页数据
     */
    @GetMapping("page")
    public ResponseResult<Page<UserVO>> getUserPage(@Validated UserQueryDTO userQueryDTO) {
        Page<UserVO> userPage = userService.getUserPage(userQueryDTO);
        return ResponseUtils.success(userPage);
    }

    /**
     * 添加用户
     *
     * @param userAddDTO 添加用户数据
     * @return
     */
    @PostMapping()
    public ResponseResult<Long> addUser(@RequestBody @Validated UserAddDTO userAddDTO) {
        Long id = userService.addUser(userAddDTO);
        return ResponseUtils.success(id);
    }

    /**
     * 更新用户
     *
     * @param userUpdateDTO 更新用户数据
     * @return 是否成功
     */
    @PutMapping()
    public ResponseResult<Boolean> updateUser(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        Boolean result = userService.updateUser(userUpdateDTO);
        return ResponseUtils.success(result);
    }

    /**
     * 删除用户
     *
     * @param deleteDTO 删除用户数据
     * @return 是否成功
     */
    @DeleteMapping()
    public ResponseResult<Boolean> deleteUser(@RequestBody @Validated DeleteDTO deleteDTO) {
        Boolean result = userService.deleteUser(deleteDTO);
        return ResponseUtils.success(result);
    }

    /**
     * 修改密码
     *
     * @param updatePasswordRequest 修改密码请求对象
     * @return 是否成功
     */
    @PutMapping("pass")
    public ResponseResult<Boolean> updatePassword(@RequestBody @Validated UserUpdatePasswordRequest updatePasswordRequest) {
        Boolean result = userService.updatePassword(updatePasswordRequest);
        return ResponseUtils.success(result);
    }

}
