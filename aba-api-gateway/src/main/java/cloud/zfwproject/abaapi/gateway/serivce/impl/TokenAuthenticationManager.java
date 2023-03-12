package cloud.zfwproject.abaapi.gateway.serivce.impl;

import cloud.zfwproject.abaapi.common.constant.RedisConstants;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 46029
 * @version 1.0
 * @description 认证处理
 * @date 2023/3/11 20:55
 */
@Component
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .mapNotNull(auth -> {
                    // 2.获取用户信息
                    String token = auth.getPrincipal().toString();
                    String tokenKey = RedisConstants.USER_LOGIN_KEY_PREFIX + token;
                    String userJSONStr = stringRedisTemplate.opsForValue().get(tokenKey);
                    if (StrUtil.isBlank(userJSONStr)) {
                        return null;
                    }
                    SimpleUser simpleUser = JSONUtil.toBean(userJSONStr, SimpleUser.class, false);
                    UserHolder.saveUser(simpleUser);
                    return simpleUser;
                })
                .map(claims -> {
                    // 获取用户权限
                    List<SimpleGrantedAuthority> authorities = claims.getPermissions().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    return new UsernamePasswordAuthenticationToken(
                            claims.getUsername(),
                            null,
                            authorities
                    );
                });
    }
}
