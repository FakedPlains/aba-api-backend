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
}
