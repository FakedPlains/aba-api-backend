package cloud.zfwproject.abaapi.service.controller;

import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.model.dto.UserRegisterDTO;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

}
