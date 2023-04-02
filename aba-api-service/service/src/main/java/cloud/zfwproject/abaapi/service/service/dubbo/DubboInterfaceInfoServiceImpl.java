package cloud.zfwproject.abaapi.service.service.dubbo;

import cloud.zfwproject.abaapi.service.model.po.InterfaceInfo;
import cloud.zfwproject.abaapi.service.remote.DubboInterfaceInfoService;
import cloud.zfwproject.abaapi.service.service.InterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/2 15:18
 */
@Service
@DubboService
public class DubboInterfaceInfoServiceImpl implements DubboInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public InterfaceInfo getInterfaceUrlByDataId(String dataId) {
        return interfaceInfoService.getInterfaceInfoByDataId(dataId);
    }

}
