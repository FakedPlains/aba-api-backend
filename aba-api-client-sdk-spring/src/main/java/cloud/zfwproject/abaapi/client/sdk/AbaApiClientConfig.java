package cloud.zfwproject.abaapi.client.sdk;

import cloud.zfwproject.abaapi.client.sdk.client.AbaApiClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/16 21:09
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(AbaApiClientProperties.class)
public class AbaApiClientConfig {

    @Resource
    private AbaApiClientProperties abaApiClientProperties;

    @Bean
    public AbaApiClient abaApiClient() {
        return new AbaApiClient(abaApiClientProperties.getAccessKey(), abaApiClientProperties.getSecretKey());
    }
}
