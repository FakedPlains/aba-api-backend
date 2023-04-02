package cloud.zfwproject.abaapi.service.remote;

import cloud.zfwproject.abaapi.service.model.po.InterfaceInfo;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/2 15:18
 */
public interface DubboInterfaceInfoService {
    InterfaceInfo getInterfaceUrlByDataId(String dataId);
}
