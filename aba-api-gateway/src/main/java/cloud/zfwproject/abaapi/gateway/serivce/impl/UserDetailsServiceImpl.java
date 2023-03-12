package cloud.zfwproject.abaapi.gateway.serivce.impl;

import cloud.zfwporject.abaapi.remote.DubboUserService;
import cloud.zfwproject.abaapi.gateway.model.SecurityUserDetails;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/11 20:15
 */
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    @DubboReference
    private DubboUserService dubboUserService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        // TODO 根据用户名查询数据库用户
        String user = dubboUserService.getUserByUserAccount(username);
        SecurityUserDetails securityUserDetails = new SecurityUserDetails("admin1", user, true, true, true, true, new ArrayList<>(), 1L);
        return Mono.just(securityUserDetails);
    }
}
