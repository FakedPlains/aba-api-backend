package cloud.zfwproject.abaapi.client.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/9 21:41
 */
@EnableDubbo
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ClientGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientGatewayApplication.class, args);
    }
}
