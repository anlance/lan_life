package club.anlan.lanlife.component.base.monitor;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池监控
 *
 * @author lan
 * @version 1.0
 * @date 2024/8/5 0:40
 */
@Component
@Slf4j
public class ThreadPoolMonitor implements InitializingBean {

    private static final String EXECUTOR_NAME = "ThreadPoolMonitorSample";
    private static final Iterable<Tag> TAG = Collections.singletonList(Tag.of("thread.pool.name", EXECUTOR_NAME));
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10), new ThreadFactory() {

        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("thread-pool-" + counter.getAndIncrement());
            return thread;
        }
    }, new ThreadPoolExecutor.AbortPolicy());

    @Resource
    private ThreadPoolTaskExecutor globalExecutor;


    private Runnable monitor = () -> {
        //这里需要捕获异常,尽管实际上不会产生异常,但是必须预防异常导致调度线程池线程失效的问题
        try {
            Metrics.gauge("thread.pool.core.size", TAG, executor, ThreadPoolExecutor::getCorePoolSize);
            Metrics.gauge("thread.pool.largest.size", TAG, executor, ThreadPoolExecutor::getLargestPoolSize);
            Metrics.gauge("thread.pool.max.size", TAG, executor, ThreadPoolExecutor::getMaximumPoolSize);
            Metrics.gauge("thread.pool.active.size", TAG, executor, ThreadPoolExecutor::getActiveCount);
            Metrics.gauge("thread.pool.thread.count", TAG, executor, ThreadPoolExecutor::getPoolSize);
            // 注意如果阻塞队列使用无界队列这里不能直接取size
            Metrics.gauge("thread.pool.queue.size", TAG, executor, e -> e.getQueue().size());
        } catch (Exception e) {
            log.error("monitor异常,", e);
        }
    };

    @Override
    public void afterPropertiesSet() throws Exception {
        // 每5秒执行一次
        scheduledExecutor.scheduleWithFixedDelay(monitor, 0, 5, TimeUnit.SECONDS);
    }

    public void shortTimeWork() {
        executor.execute(() -> {
            try {
                // 5秒
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //ignore
            }
        });
    }

    public void longTimeWork() {
        executor.execute(() -> {
            try {
                // 500秒
                Thread.sleep(5000 * 100);
            } catch (InterruptedException e) {
                //ignore
            }
        });
    }

    public void clearTaskQueue() {
        executor.getQueue().clear();
    }
}
