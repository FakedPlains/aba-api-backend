package cloud.zfwproject.abaapi.gateway.filter;

import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/25 11:05
 */
@Component
public class GlobalLoginFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();
        if (path.contains("login") || path.contains("register")) {
            return chain.filter(exchange);
        }

        // 1.获取用户信息，判断用户是否存在
        SimpleUser user = UserHolder.getUser();
        if (user == null) {
            // 2.不存在，拦截
            return ResponseUtils.outFailed(response, ResponseUtils.fail(ResponseCode.PERMISSION_DENIED));
        }

        // 3.存在，放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
