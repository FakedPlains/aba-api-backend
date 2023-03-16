package cloud.zfwproject.abaapi.service.service;

import cloud.zfwproject.abaapi.service.model.dto.DeleteDTO;
import cloud.zfwproject.abaapi.service.model.dto.user.UserAddDTO;
import cloud.zfwproject.abaapi.service.model.dto.user.UserQueryDTO;
import cloud.zfwproject.abaapi.service.model.dto.user.UserRegisterDTO;
import cloud.zfwproject.abaapi.service.model.dto.user.UserUpdateDTO;
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
     * @param userQueryDTO 用户查询分页庆请求
     * @return 分页数据
     */
    Page<UserVO> getUserPage(UserQueryDTO userQueryDTO);


    /**
     * 添加用户
     * @param userAddDTO 添加用户数据
     * @return 用户 id
     */
    Long addUser(UserAddDTO userAddDTO);

    /**
     * 更新用户
     *
     * @param userUpdateDTO 更新用户数据
     * @return 是否成功
     */
    Boolean updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * 删除用户
     *
     * @param deleteDTO 删除用户数据
     * @return 是否成功
     */
    Boolean deleteUser(DeleteDTO deleteDTO);
}
