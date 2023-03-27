package cloud.zfwproject.abaapi.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/9 21:40
 */
@EnableDubbo
@SpringBootApplication(scanBasePackages = {"cloud.zfwproject.abaapi"})
@EnableDiscoveryClient
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
