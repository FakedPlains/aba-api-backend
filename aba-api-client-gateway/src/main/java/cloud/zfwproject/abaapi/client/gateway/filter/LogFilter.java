package cloud.zfwproject.abaapi.client.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/2 20:28
 */
@Slf4j
//@Component
public class LogFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识: {}", request.getId());
        String path = request.getPath().pathWithinApplication().value();
        log.info("请求路径: {}", path);
        log.info("请求方法: {}", request.getMethod());
        log.info("请求参数: {}", request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源: {}", sourceAddress);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
