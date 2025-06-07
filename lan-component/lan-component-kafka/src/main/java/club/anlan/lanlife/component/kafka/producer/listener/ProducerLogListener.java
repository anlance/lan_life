package club.anlan.lanlife.component.kafka.producer.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * ProducerLogListener
 *
 * @author lan
 * @version 1.0
 * @date 2024/12/31 10:09
 */
@Component
@Slf4j
public class ProducerLogListener implements ProducerListener {

    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("onSuccess : {} ", recordMetadata);
    }

    @Override
    public void onError(ProducerRecord producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("onError : {} ", recordMetadata);
    }

}
