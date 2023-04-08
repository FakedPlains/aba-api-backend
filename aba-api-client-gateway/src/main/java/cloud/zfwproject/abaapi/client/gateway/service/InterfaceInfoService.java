package cloud.zfwproject.abaapi.client.gateway.service;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/8 9:12
 */
public interface InterfaceInfoService {
    /**
     * 根据 dataId 获取接口 URL
     * @param dataId
     * @return
     */
    String getInterfaceUrlByDataId(String dataId);

    boolean invokeInterface(String accessKey, String dataId);
}
