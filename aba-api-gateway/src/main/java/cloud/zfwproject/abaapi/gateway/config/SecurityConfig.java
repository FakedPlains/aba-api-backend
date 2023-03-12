package cloud.zfwproject.abaapi.gateway.config;

import cloud.zfwproject.abaapi.gateway.handler.*;
import cloud.zfwproject.abaapi.gateway.serivce.impl.DefaultAuthorizationManager;
import cloud.zfwproject.abaapi.gateway.serivce.impl.DefaultSecurityContextRepository;
import cloud.zfwproject.abaapi.gateway.serivce.impl.TokenAuthenticationManager;
import cloud.zfwproject.abaapi.gateway.serivce.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.LinkedList;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/9 16:34
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Resource
    private DefaultAuthorizationManager defaultAuthorizationManager;

    @Resource
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Resource
    private DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;

    @Resource
    private DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler;

    @Resource
    private TokenAuthenticationManager tokenAuthenticationManager;

    @Resource
    private DefaultSecurityContextRepository defaultSecurityContextRepository;

    @Resource
    private DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;

    @Resource
    private DefaultAccessDeniedHandler defaultAccessDeniedHandler;

    @Resource
    private TokenLogoutHandler tokenLogoutHandler;

    /**
     * 自定义过滤权限
     */
    @Value("${security.noFilter}")
    private String noFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                // 登录认证处理
                .authenticationManager(reactiveAuthenticationManager())
                .securityContextRepository(defaultSecurityContextRepository)
                // 请求拦截处理
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(noFilter).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().access(defaultAuthorizationManager)
                )
                .formLogin()
                .loginPage("")
                // 自定义处理
                .authenticationSuccessHandler(defaultAuthenticationSuccessHandler)
                .authenticationFailureHandler(defaultAuthenticationFailureHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(defaultAuthenticationEntryPoint)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(defaultAccessDeniedHandler)
                .and()
                .csrf().disable()
                // 退出登录
                .logout()
                .logoutUrl("")
                .logoutHandler(tokenLogoutHandler);
        return httpSecurity.build();
    }

    // 密码加密方式
    @Bean("passwordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注册用户信息验证管理器，可按需求添加多个按顺序执行
     */
    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager() {
        LinkedList<ReactiveAuthenticationManager> managers = new LinkedList<>();
        managers.add(authentication -> {
            // 其他登陆方式 (比如手机号验证码登陆) 可在此设置不得抛出异常或者 Mono.error
            return Mono.empty();
        });
        // 必须放最后不然会优先使用用户名密码校验但是用户名密码不对时此 AuthenticationManager 会调用 Mono.error 造成后面的 AuthenticationManager 不生效
        managers.add(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsServiceImpl));
        managers.add(tokenAuthenticationManager);
        return new DelegatingReactiveAuthenticationManager(managers);
    }

}
