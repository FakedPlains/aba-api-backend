package cloud.zfwproject.abaapi.service.remote;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/12 16:15
 */
public interface DubboUserService {
    String getUserByUserAccount(String userAccount);

    String getUserByAccessKey(String accessKey);
}
