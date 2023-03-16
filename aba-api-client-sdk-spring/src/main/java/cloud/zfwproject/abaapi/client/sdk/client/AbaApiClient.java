package cloud.zfwproject.abaapi.client.sdk.client;

import cloud.zfwproject.abaapi.client.sdk.model.GetRequest;
import cloud.zfwproject.abaapi.client.sdk.util.SignUtils;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/16 21:12
 */
public class AbaApiClient {

    private String accessKey;
    private String secretKey;

    private static final String HOST = "http://localhost:20001/";

    public AbaApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String get(GetRequest getRequest) {
        return HttpRequest.get(HOST + "get")
                .addHeaders(getHeaderMap(getRequest.getName()))
                .body(getRequest.getName())
                .execute()
                .body();
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> header = new HashMap<>();
        header.put("accessKey", accessKey);
        header.put("nonce", RandomUtil.randomNumbers(4));
        header.put("body", body);
        header.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        header.put("sign", SignUtils.getSign(body, secretKey));
        return header;
    }
}
