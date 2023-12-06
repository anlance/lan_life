package club.anlan.lanlife.kafka.consumer;

import club.anlan.lanlife.kafka.KafkaTopics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * kafka consumer
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/5 17:15
 */
@Slf4j
@Component
@AllArgsConstructor
public class KafkaConsumer {


    @KafkaListener(topics = {KafkaTopics.TEST_TOPIC}, errorHandler = "logKafkaListenerErrorHandler")
    public void onMessage(ConsumerRecord<Integer, String> record) {
        log.info("received message [{}] from [{}]", record.value(), KafkaTopics.TEST_TOPIC);
    }

    @KafkaListener(topics = {KafkaTopics.TEST_TOPIC1})
    public void onMessage1(String msg, @Header("traceId") byte[] traceId) {
        log.info("traceId:{}", new String(traceId));
        log.info("received message [{}] from [{}]", msg, KafkaTopics.TEST_TOPIC1);
    }

    @KafkaListener(topics = {KafkaTopics.TEST_TOPIC2})
    public void onMessage2(String msg, @Headers Map<String, Object> traceId) {
        log.info("traceId:{}", traceId);
        log.info("received message [{}] from [{}]", msg, KafkaTopics.TEST_TOPIC2);
    }


    //@KafkaListener(topics = {KafkaTopics.TEST_TOPIC})
    //public void process(String msg) {
    //    log.info("received message [{}] from [{}]", msg, KafkaTopics.TEST_TOPIC);
    //}

}
