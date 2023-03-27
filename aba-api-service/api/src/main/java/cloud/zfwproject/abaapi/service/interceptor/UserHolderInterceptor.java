package cloud.zfwproject.abaapi.service.interceptor;

import cloud.zfwproject.abaapi.common.constant.RedisConstants;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cloud.zfwproject.abaapi.common.constant.CommonConstant.REQUEST_HEADER_TOKEN;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/27 17:49
 */
@Component
public class UserHolderInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的 token
        String token = request.getHeader(REQUEST_HEADER_TOKEN);

        // 2.获取 redis 中的用户
        String tokenKey = RedisConstants.USER_LOGIN_KEY_PREFIX + token;
        String userStr = stringRedisTemplate.opsForValue().get(tokenKey);

        SimpleUser simpleUser = JSONUtil.toBean(userStr, SimpleUser.class);
        UserHolder.saveUser(simpleUser);

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
