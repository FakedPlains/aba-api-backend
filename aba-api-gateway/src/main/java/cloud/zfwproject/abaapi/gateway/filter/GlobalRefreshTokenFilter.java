package cloud.zfwproject.abaapi.gateway.filter;

import cloud.zfwproject.abaapi.common.constant.RedisConstants;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static cloud.zfwproject.abaapi.common.constant.CommonConstant.REQUEST_HEADER_TOKEN;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/25 10:40
 */
@Component
public class GlobalRefreshTokenFilter implements GlobalFilter, Ordered {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 1.获取请求头中的 token
        String token = request.getHeaders().getFirst(REQUEST_HEADER_TOKEN);
        if (StrUtil.isBlank(token)) {
            return chain.filter(exchange);
        }

        // 2.获取 redis 中的用户
        String tokenKey = RedisConstants.USER_LOGIN_KEY_PREFIX + token;
        String userStr = stringRedisTemplate.opsForValue().get(tokenKey);

        // 3.判断用户是否存在
        if (StrUtil.isBlank(userStr)) {
            return chain.filter(exchange);
        }

        // 4.转换为 SimpleUser，保存到 ThreadLocal
//        SimpleUser simpleUser = BeanUtil.fillBeanWithMap(userMap, new SimpleUser(), false);
        SimpleUser simpleUser = JSONUtil.toBean(userStr, SimpleUser.class);
        UserHolder.saveUser(simpleUser);

        // 5.刷新 token 有效期
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(UserHolder::removeUser));
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
