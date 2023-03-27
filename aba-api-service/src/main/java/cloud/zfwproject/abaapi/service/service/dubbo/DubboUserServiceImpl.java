package cloud.zfwproject.abaapi.service.service.dubbo;

import cloud.zfwporject.abaapi.remote.DubboUserService;
import cloud.zfwproject.abaapi.service.model.po.User;
import cloud.zfwproject.abaapi.service.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/12 16:17
 */
@Service
@DubboService
public class DubboUserServiceImpl implements DubboUserService {

    @Resource
    private UserService userService;

    @Override
    public String getUserByUserAccount(String userAccount) {
        User user = userService.getUserByUserAccount(userAccount);
        return user.getUserPassword();
    }

    @Override
    public String getUserByAccessKey(String accessKey) {
        User user = userService.getUserByAccessKey(accessKey);
        return user == null ? null : user.getSecretKey();
    }
}
