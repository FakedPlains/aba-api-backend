package cloud.zfwproject.abaapi.service.service;

import cloud.zfwproject.abaapi.service.model.dto.user.UserQueryRequest;
import cloud.zfwproject.abaapi.service.model.dto.user.UserRegisterDTO;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.model.vo.UserVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 46029
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-03-12 13:44:10
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册数据
     * @return 用户 id
     */
    Long register(UserRegisterDTO userRegisterDTO);

    /**
     * 根据用户账号获取用户
     *
     * @param userAccount 用户账号
     * @return 用户
     */
    User getUserByUserAccount(String userAccount);

    /**
     * 分页获取用户数据
     *
     * @param userQueryRequest 用户查询分页庆请求
     * @return 分页数据
     */
    Page<UserVO> getUserPage(UserQueryRequest userQueryRequest);
}
