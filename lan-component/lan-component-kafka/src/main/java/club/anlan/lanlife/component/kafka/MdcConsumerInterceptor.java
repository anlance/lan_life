package club.anlan.lanlife.component.kafka;

import club.anlan.lanlife.component.utils.UUIDUtil;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/14 22:46
 */
public class MdcConsumerInterceptor implements ConsumerInterceptor {
    @Override
    public ConsumerRecords onConsume(ConsumerRecords records) {
        return records;
    }

    @Override
    public void close() {

    }

    @Override
    public void onCommit(Map offsets) {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
