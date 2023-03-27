package cloud.zfwproject.abaapi.service.config;

import cloud.zfwproject.abaapi.service.interceptor.UserHolderInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/27 17:52
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private UserHolderInterceptor userHolderInterceptor;

    /**
     * 配置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userHolderInterceptor)
                .addPathPatterns("/**");
    }

}
