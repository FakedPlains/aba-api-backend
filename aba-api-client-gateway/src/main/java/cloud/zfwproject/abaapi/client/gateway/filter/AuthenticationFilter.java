package cloud.zfwproject.abaapi.client.gateway.filter;

import cloud.zfwproject.abaapi.client.sdk.util.SignUtils;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.remote.DubboUserService;
import cn.hutool.core.util.StrUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/2 17:30
 */
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @DubboReference
    private DubboUserService userService;

    private static final List<String> IP_WHITE_LIST = List.of("127.0.0.1", "0:0:0:0:0:0:0:1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse res = exchange.getResponse();
        String sourceAddress = request.getLocalAddress().getHostString();

        // 2.黑白名单
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return ResponseUtils.outFailed(res, ResponseUtils.fail(ResponseCode.PERMISSION_DENIED));
        }
        // 3.用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");

        String secretKey = userService.getUserByAccessKey(accessKey);
        if (StrUtil.isBlank(secretKey)) {
            return ResponseUtils.outFailed(res, ResponseUtils.fail(ResponseCode.PERMISSION_DENIED));
        }
        if (StrUtil.isBlank(nonce) || Long.parseLong(nonce) > 10000L) {
            return ResponseUtils.outFailed(res, ResponseUtils.fail(ResponseCode.PERMISSION_DENIED));
        }
        long current = System.currentTimeMillis() / 1000;
        long FIVE_MINUTES = 60 * 5L;
        if (StrUtil.isBlank(timestamp) || (current - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return ResponseUtils.outFailed(res, ResponseUtils.fail(ResponseCode.PERMISSION_DENIED));
        }

        String serverSign = SignUtils.getSign(body, secretKey);
        if (StrUtil.isBlank(sign) || !sign.equals(serverSign)) {
            return ResponseUtils.outFailed(res, ResponseUtils.fail(ResponseCode.PERMISSION_DENIED));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
