package cloud.zfwproject.abaapi.service.controller;

import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.model.dto.user.UserQueryRequest;
import cloud.zfwproject.abaapi.service.model.dto.user.UserRegisterDTO;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.model.vo.UserVO;
import cloud.zfwproject.abaapi.service.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 分页获取用户数据
     *
     * @param userQueryRequest 用户查询分页庆请求
     * @return 分页数据
     */
    @GetMapping("page")
    public ResponseResult<Page<UserVO>> getUserPage(@Validated UserQueryRequest userQueryRequest) {
        Page<UserVO> userPage = userService.getUserPage(userQueryRequest);
        return ResponseUtils.success(userPage);
    }

}
