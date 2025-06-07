package club.anlan.lanlife.proxy.server.demo;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserExcelListener extends AnalysisEventListener<User> {
    /**
     * 每隔100条处理下，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;


    /**
     * 缓存的数据
     */
    private List<User> cachedDataList = new ArrayList<>(100);


    /**
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("======>>>解析异常：", exception);
        throw exception;
    }

    /**
     * 当读取到一行数据时，会调用这个方法，并将读取到的数据以及上下文信息作为参数传入
     * 可以在这个方法中对读取到的数据进行处理和操作，处理数据时要注意异常错误，保证读取数据的稳定性
     *
     * @param user
     * @param context
     */
    @Override
    public void invoke(User user, AnalysisContext context) {
        log.info("解析到一条数据:{}", user);
        cachedDataList.add(user);
        if (cachedDataList.size() >= BATCH_COUNT) {
            // 处理缓存的数据，比如说入库。。。
            // 然后清空
            cachedDataList.clear();
        }
    }

    /**
     * 当每个sheet所有数据读取完毕后，会调用这个方法，可以在这个方法中进行一些收尾工作，如资源释放、数据汇总等。
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 收尾工作，处理剩下的缓存数据。。。

        log.info("sheet={} 所有数据解析完成！", context.readSheetHolder().getSheetName());
    }

}

