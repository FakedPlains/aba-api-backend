package cloud.zfwproject.abaapi.service.config;

import cloud.zfwproject.abaapi.service.handler.AsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/7 21:25
 */
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    //阻塞队列
    private static final int workQueue = 20;
    //线程空闲后的存活时长
    private static final int keepAliveTime = 30;
    //Cpu核数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数量大小
    private static final int corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    //线程池最大容纳线程数
    private static final int maxPoolSize = CPU_COUNT * 2 + 1;

    @Bean("asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //线程前缀
        threadPoolTaskExecutor.setThreadNamePrefix("asyncTaskExecutor-");
        //核心线程数
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        //最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        //等待队列
        threadPoolTaskExecutor.setQueueCapacity(workQueue);
        //线程池维护线程所允许的空闲时间,单位为秒
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveTime);
        // 线程池对拒绝任务(无线程可用)的处理策略
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return asyncTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

}
