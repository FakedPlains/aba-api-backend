package cloud.zfwproject.abaapi.client.sdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/13 21:13
 */
@Data
@ConfigurationProperties("abaapi.client")
public class AbaApiClientProperties {

    private String accessKey;
    private String secretKey;

}
