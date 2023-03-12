package cloud.zfwproject.abaapi.gateway.handler;

import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.gateway.model.SecurityUserDetails;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static cloud.zfwproject.abaapi.common.constant.RedisConstants.LOGIN_USER_TTL;
import static cloud.zfwproject.abaapi.common.constant.RedisConstants.USER_LOGIN_KEY_PREFIX;

/**
 * @author 46029
 * @version 1.0
 * @description 登录成功处理器
 * @date 2023/3/11 20:22
 */
@Component
public class DefaultAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return Mono.defer(() -> Mono.just(webFilterExchange.getExchange().getResponse())
                .flatMap(response -> {
                    // 获取登录用户
                    SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
                    // 1.随机生成 token 作为登录令牌
                    String token = IdUtil.simpleUUID();
                    // 2.保存用户到 redis
                    String tokenKey = USER_LOGIN_KEY_PREFIX + token;
                    SimpleUser simpleUser = BeanUtil.copyProperties(userDetails, SimpleUser.class);
//            simpleUser.setPermissions(user.getPermissions());
//            simpleUser.setRouters(user.getRouters());
//            redisUtils.setWithStr(tokenKey, simpleUser, LOGIN_USER_TTL, TimeUnit.MINUTES);
                    stringRedisTemplate.opsForValue().set(tokenKey, JSONUtil.toJsonStr(simpleUser), LOGIN_USER_TTL, TimeUnit.MINUTES);
                    // 3.返回 token 给前端
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    ResponseResult<String> result = ResponseUtils.success(token);
                    DataBuffer dataBuffer = dataBufferFactory.wrap(JSONUtil.toJsonStr(result).getBytes());
                    return response.writeWith(Mono.just(dataBuffer));
                }));
    }
}
