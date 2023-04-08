package cloud.zfwproject.abaapi.service.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/8 17:25
 */
@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("{} 执行错误：{}", method.getName(), ex.getMessage());
    }
}
