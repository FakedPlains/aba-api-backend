package cloud.zfwproject.abaapi.gateway.serivce.impl;

import cloud.zfwproject.abaapi.common.constant.CommonConstant;
import cn.hutool.core.util.StrUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 46029
 * @version 1.0
 * @description 存储认证授权相关信息
 * @date 2023/3/11 20:52
 */
@Component
public class DefaultSecurityContextRepository implements ServerSecurityContextRepository {

    @Resource
    private TokenAuthenticationManager tokenAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> headers = request.getHeaders().get(CommonConstant.REQUEST_HEADER_TOKEN);
        if (!CollectionUtils.isEmpty(headers)) {
            String token = headers.get(0);
            if (StrUtil.isNotBlank(token)) {
                return tokenAuthenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(token, null))
                        .map(SecurityContextImpl::new);
            }
        }
        return Mono.empty();
    }
}
