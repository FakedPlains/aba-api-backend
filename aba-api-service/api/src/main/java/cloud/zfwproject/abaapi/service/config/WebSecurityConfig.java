package cloud.zfwproject.abaapi.service.config;

import cloud.zfwproject.abaapi.service.encoder.MD5PasswordEncoder;
import cloud.zfwproject.abaapi.service.encoder.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/13 13:18
 */
@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }

}
