package cloud.zfwproject.abaapi.client.gateway.service.impl;

import cloud.zfwproject.abaapi.client.gateway.service.InterfaceInfoService;
import cloud.zfwproject.abaapi.common.constant.RedisConstants;
import cloud.zfwproject.abaapi.common.service.RedisService;
import cloud.zfwproject.abaapi.service.remote.DubboUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/8 9:12
 */
@Service("interfaceInfoService")
public class InterfaceInfoServiceImpl implements InterfaceInfoService {

    @Resource
    private RedisService redisService;

    @DubboReference
    private DubboUserInterfaceInfoService userInterfaceInfoService;

    @Override
    public String getInterfaceUrlByDataId(String dataId) {
        return redisService.getFromHashKey(RedisConstants.INTERFACE_INFO_PREFIX, dataId);
    }

    @Override
    public synchronized boolean invokeInterface(String accessKey, String dataId) {
        int leftCount = userInterfaceInfoService.getInvokeLeftCount(accessKey, dataId);
        if (leftCount <= 0) {
            return false;
        }
        userInterfaceInfoService.incrementInvokeCount(accessKey, dataId, 1);
        return true;
    }
}
