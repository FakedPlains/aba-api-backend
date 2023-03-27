package cloud.zfwproject.abaapi.client.gateway.filter;

import cloud.zfwporject.abaapi.remote.DubboUserService;
import cloud.zfwproject.abaapi.client.sdk.util.SignUtils;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 46029
 * @version 1.0
 * @description 全局过滤器
 * @date 2023/3/3 13:05
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private DubboUserService userService;

    private static final List<String> IP_WHITE_LIST = List.of("127.0.0.1", "0:0:0:0:0:0:0:1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识: {}", request.getId());
        String path = request.getPath().pathWithinApplication().value();
        log.info("请求路径: {}", path);
        log.info("请求方法: {}", request.getMethod());
        log.info("请求参数: {}", request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源: {}", sourceAddress);
        ServerHttpResponse res = exchange.getResponse();
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
            throw new RuntimeException("无权限");
        }
        // TODO 4.请求模拟接口是否存在
        // 5.请求转发，调用接口
        return handleResponse(exchange, chain);
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // TODO 7.请求次数 +1
                                // 6.记录响应日志
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8); // data
                                sb2.append(data);
                                log.info(sb2.toString(), rspArgs.toArray()); // log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return -2;
    }

}
