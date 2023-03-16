package cloud.zfwproject.abaapi.client.sdk;

import cloud.zfwproject.abaapi.client.sdk.client.AbaApiClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/16 21:09
 */
@Configuration
@ComponentScan
@ConfigurationProperties("abaapi.client")
public class AbaApiClientConfig {

    private String accessKey;
    private String secretKey;

    @Bean
    public AbaApiClient abaApiClient() {
        return new AbaApiClient(accessKey, secretKey);
    }
}
