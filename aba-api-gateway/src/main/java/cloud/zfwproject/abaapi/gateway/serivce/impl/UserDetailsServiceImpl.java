package cloud.zfwproject.abaapi.gateway.serivce.impl;

import cloud.zfwproject.abaapi.gateway.model.SecurityUserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/11 20:15
 */
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        // TODO 根据用户名查询数据库用户
        SecurityUserDetails securityUserDetails = new SecurityUserDetails("user", passwordEncoder.encode("user"), true, true, true, true, new ArrayList<>(), 1L);
        return Mono.just(securityUserDetails);
    }
}
