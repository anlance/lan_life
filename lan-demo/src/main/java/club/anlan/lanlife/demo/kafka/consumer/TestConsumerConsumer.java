package club.anlan.lanlife.demo.kafka.consumer;

import club.anlan.lanlife.demo.domain.TestKafkaParam;
import club.anlan.lanlife.demo.kafka.KafkaConstants;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 测试消息消费者
 *
 * @author lan
 * @version 1.0
 * @date 2021/8/15 22:10
 */
@Component
@Slf4j
public class TestConsumerConsumer {

    @KafkaListener(id = "2a121f7b1d402825", topics = KafkaConstants.TEST_SECOND_TOPIC)
    public void onMessage(ConsumerRecord<String, String> record) {
        log.info("【接收】");
        if (null != record) {
            log.info("【接收】-【{}】 key : {}, value : {}", KafkaConstants.TEST_SECOND_TOPIC, record.key(), record.value());
        }
    }
}
