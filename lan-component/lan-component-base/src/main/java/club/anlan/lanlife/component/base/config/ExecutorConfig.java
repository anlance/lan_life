package club.anlan.lanlife.component.base.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;


/**
 * 线程池
 *
 * @author lan
 * @version 1.0
 * @date 2024/8/5 0:37
 */
@Configuration
@Slf4j
public class ExecutorConfig {
    @Bean(name = "globalExecutor")
    public ThreadPoolTaskExecutor globalExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("globalExecutor-");
        executor.setTaskDecorator(runnable -> {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                try {
                    MDC.setContextMap(contextMap);
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        });
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn("global线程池已满"));
        executor.initialize();
        return executor;
    }
}
