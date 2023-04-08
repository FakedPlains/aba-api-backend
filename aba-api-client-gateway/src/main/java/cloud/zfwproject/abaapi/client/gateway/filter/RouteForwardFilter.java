package cloud.zfwproject.abaapi.client.gateway.filter;

import cloud.zfwproject.abaapi.client.gateway.service.InterfaceInfoService;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/2 17:31
 */
@Slf4j
@Component
public class RouteForwardFilter implements GlobalFilter, Ordered {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse res = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String dataId = headers.getFirst("dataId");

        // 4.请求模拟接口是否存在
        String url = interfaceInfoService.getInterfaceUrlByDataId(dataId);
        if (url == null) {
            return ResponseUtils.outFailed(res, ResponseUtils.fail(ResponseCode.INVALID_PARAMS, "接口不存在"));
        }
        // 调用次数 + 1
        boolean canInvoke = interfaceInfoService.invokeInterface(accessKey, dataId);
        if (!canInvoke) {
            return ResponseUtils.outFailed(res, ResponseUtils.fail(ResponseCode.OPERATION_ERROR, "调用次数不足"));
        }

        // 5.请求转发，调用接口
        String rawPath = request.getURI().getRawPath();
        //请求方法
        HttpMethod method = request.getMethod();
        //请求参数
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        URI uri = UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParams).build().toUri();
        //替换新的url地址
        ServerHttpRequest serverHttpRequest = request.mutate()
                .uri(uri)
                .method(method)
                .headers(httpHeaders -> httpHeaders = headers)
                .build();
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        //从新设置Route地址
        Route newRoute = Route.async()
                .asyncPredicate(route.getPredicate())
                .filters(route.getFilters())
                .id(route.getId())
                .order(route.getOrder())
                .uri(uri)
                .build();
        exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, newRoute);
        return chain.filter(exchange.mutate().request(serverHttpRequest).build());
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
