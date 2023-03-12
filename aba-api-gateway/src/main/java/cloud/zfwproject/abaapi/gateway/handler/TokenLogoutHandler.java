package cloud.zfwproject.abaapi.gateway.handler;

import cloud.zfwproject.abaapi.common.constant.CommonConstant;
import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

import static cloud.zfwproject.abaapi.common.constant.RedisConstants.USER_LOGIN_KEY_PREFIX;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/1/18 17:21
 */
@Component
public class TokenLogoutHandler implements ServerLogoutHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
        return Mono.defer(() -> Mono.just(exchange.getExchange())
                .flatMap(webExchange -> {
                    ServerHttpRequest request = webExchange.getRequest();
                    ServerHttpResponse response = webExchange.getResponse();
                    List<String> headers = request.getHeaders().get(CommonConstant.REQUEST_HEADER_TOKEN);
                    if (CollectionUtil.isEmpty(headers)) {
                        return Mono.empty();
                    }
                    String token = headers.get(0);
                    if (StrUtil.isBlank(token)) {
                        return Mono.empty();
                    }
                    String tokenKey = USER_LOGIN_KEY_PREFIX + token;
                    stringRedisTemplate.delete(tokenKey);
                    UserHolder.removeUser();
                    // 3.返回 token 给前端
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    ResponseResult<String> result = ResponseUtils.success();
                    DataBuffer dataBuffer = dataBufferFactory.wrap(JSONUtil.toJsonStr(result).getBytes());
                    return response.writeWith(Mono.just(dataBuffer));
                }));
    }
}
