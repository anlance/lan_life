package club.anlan.lanlife.kafka.producer;

import club.anlan.lanlife.component.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/13 17:56
 */
@Slf4j
public class MdcProducerInterceptor implements ProducerInterceptor {
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        if (record != null) {
            if (record.headers() != null) {
                record.headers().add("traceId", UUIDUtil.generateUuid().getBytes(StandardCharsets.UTF_8));
            }
        }
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
