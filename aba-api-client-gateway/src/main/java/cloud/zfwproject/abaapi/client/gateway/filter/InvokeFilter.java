package cloud.zfwproject.abaapi.client.gateway.filter;

import cloud.zfwproject.abaapi.client.gateway.service.InterfaceInfoService;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/8 16:09
 */
@Component
public class InvokeFilter implements GlobalFilter, Ordered {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse res = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String dataId = headers.getFirst("dataId");

        // 1.请求模拟接口是否存在
        String url = interfaceInfoService.getInterfaceUrlByDataId(dataId);
        if (url == null) {
            return ResponseUtils.outFailed(res, ResponseUtils.fail(ResponseCode.INVALID_PARAMS, "接口不存在"));
        }
        // 2.判断能否调用接口
        boolean canInvoke = interfaceInfoService.invokeInterface(accessKey, dataId);
        if (!canInvoke) {
            return ResponseUtils.outFailed(res, ResponseUtils.fail(ResponseCode.OPERATION_ERROR, "调用次数不足"));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
