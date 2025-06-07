package club.anlan.lanlife.component.kafka.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * LogInterceptor
 *
 * @author lan
 * @version 1.0
 * @date 2024/12/31 9:51
 */
@Component
@Slf4j
public class LogInterceptor implements ProducerInterceptor {
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        log.info("onSend: {}", JSON.toJSONString(record));
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        log.info("onAcknowledgement: {}", JSON.toJSONString(metadata));
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
