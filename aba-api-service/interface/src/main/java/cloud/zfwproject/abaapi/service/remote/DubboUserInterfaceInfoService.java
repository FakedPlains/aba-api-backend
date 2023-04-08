package cloud.zfwproject.abaapi.service.remote;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/8 12:23
 */
public interface DubboUserInterfaceInfoService {

    int getInvokeLeftCount(String accessKey, String dataId);

    void modifyInvokeCount(String accessKey, String dataId);

}
