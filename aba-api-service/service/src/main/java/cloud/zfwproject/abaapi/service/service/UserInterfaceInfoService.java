package cloud.zfwproject.abaapi.service.service;

import cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo.UserInterfaceInfoAddDTO;
import cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo.UserInterfaceInfoQueryDTO;
import cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateDTO;
import cloud.zfwproject.abaapi.service.model.po.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 46029
 * @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Service
 * @createDate 2023-03-12 21:18:58
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    long addUserInterfaceInfo(UserInterfaceInfoAddDTO userInterfaceInfoAddDTO);

    boolean deleteUserInterfaceInfo(Long id);

    boolean updateUserInterfaceInfo(UserInterfaceInfoUpdateDTO userInterfaceInfoUpdateDTO);

    UserInterfaceInfo getUserInterfaceInfoById(long id);

    List<UserInterfaceInfo> listUserInterfaceInfo(UserInterfaceInfoQueryDTO userInterfaceInfoQueryDTO);

    Page<UserInterfaceInfo> listUserInterfaceInfoByPage(UserInterfaceInfoQueryDTO userInterfaceInfoQueryDTO);

    /**
     * 修改接口调用次数
     *
     * @param userId
     * @param interfaceInfoId
     */
    void modifyInvokeCount(Long userId, Long interfaceInfoId);

    /**
     * 获取接口剩余调用次数
     *
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    int getInvokeLeftCount(Long userId, Long interfaceInfoId);

    /**
     * 根据接口 id 获取总调用次数
     *
     * @param interfaceInfoId 接口 id
     * @return 总调用次数
     */
    long getInvokeCountByInterfaceId(Long interfaceInfoId);

    /**
     * 根据用户 id 和接口 id 获取用户接口信息
     *
     * @param userId      用户 id
     * @param interfaceId 接口 id
     * @return 用户接口信息
     */
    UserInterfaceInfo getByUserIdAndInterfaceId(Long userId, Long interfaceId);

    /**
     * 激活接口
     *
     * @param userId          用户 id
     * @param interfaceInfoId 接口 id
     */
    void activeFreeInvoke(Long userId, Long interfaceInfoId);

    /**
     * 购买接口调用次数
     *
     * @param userId          用户 id
     * @param count           调用次数
     * @param interfaceInfoId 接口 id
     * @return
     */
    void buyInterfaceInvokeCount(Long userId, Long interfaceInfoId, Long count);
}
