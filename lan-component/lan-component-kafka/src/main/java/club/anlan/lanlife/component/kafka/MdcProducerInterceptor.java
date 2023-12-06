package club.anlan.lanlife.component.kafka;

import club.anlan.lanlife.component.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class MdcProducerInterceptor implements ProducerInterceptor {
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        if (record != null) {
            if (record.headers() != null) {
                record.headers().add("traceId", UUIDUtil.generateUuid().getBytes(StandardCharsets.UTF_8));
            }
        }
        record.headers().forEach(i -> {
            log.info("{}-{}", i.key(), new String(i.value()));
        });
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