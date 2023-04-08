package cloud.zfwproject.abaapi.service.service.dubbo;

import cloud.zfwproject.abaapi.service.model.po.InterfaceInfo;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.remote.DubboUserInterfaceInfoService;
import cloud.zfwproject.abaapi.service.service.InterfaceInfoService;
import cloud.zfwproject.abaapi.service.service.UserInterfaceInfoService;
import cloud.zfwproject.abaapi.service.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/8 12:25
 */
@Service
@DubboService
public class DubboUserInterfaceInfoServiceImpl implements DubboUserInterfaceInfoService {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public int getInvokeLeftCount(String accessKey, String dataId) {
        User user = userService.getUserByAccessKey(accessKey);
        InterfaceInfo interfaceInfo = interfaceInfoService.getInterfaceInfoByDataId(dataId);
        Long userId = user.getId();
        Long interfaceInfoId = interfaceInfo.getId();
        return userInterfaceInfoService.getInvokeLeftCount(userId, interfaceInfoId);
    }

    @Override
    public void incrementInvokeCount(String accessKey, String dataId, Integer count) {
        User user = userService.getUserByAccessKey(accessKey);
        InterfaceInfo interfaceInfo = interfaceInfoService.getInterfaceInfoByDataId(dataId);
        userInterfaceInfoService.incrementInvokeCount(user.getId(), interfaceInfo.getId(), count);
    }
}
