package cloud.zfwproject.abaapi.gateway.handler;

import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cn.hutool.json.JSONUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * @author 46029
 * @version 1.0
 * @description 鉴权管理
 * @date 2023/3/11 20:41
 */
@Component
public class DefaultAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    String result = JSONUtil.toJsonStr(ResponseUtils.fail(ResponseCode.PERMISSION_DENIED));
                    DataBuffer buffer = dataBufferFactory.wrap(result.getBytes(Charset.defaultCharset()));
                    return response.writeWith(Mono.just(buffer));
                });
    }
}