package club.anlan.lanlife.basic.thread;

import java.util.*;
import java.util.concurrent.*;

/**
 * MyThreadPool
 * 对线程进行分组，组内排队按序号进行
 * 组间并发执行
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/16 15:39
 */
public class MyThreadPool extends ThreadPoolExecutor {

    private static Map<String, String> idMap = new HashMap<String, String>();

    /**
     * taskId : index
     */
    private static Map<String, Integer> indexMap = new HashMap<String, Integer>();

    /**
     * taskId-index : status
     */
    private static Map<String, String> statusMap = new HashMap<String, String>();

    private static Map<String, List<Callable>> groupTaskMap = new HashMap<>();

    public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

    // 分组
    protected void group(Callable task, String taskId) {
        if(groupTaskMap.containsKey(taskId)) {
            groupTaskMap.get(taskId).add(task);
        } else {
            groupTaskMap.put(taskId, Arrays.asList(task));
        }
    };

    // 提交任务
    public Future<?> submit(Callable task, String taskId, String index) {
        // 分组
        group(task, taskId);
        return super.submit(task);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
    }


}
