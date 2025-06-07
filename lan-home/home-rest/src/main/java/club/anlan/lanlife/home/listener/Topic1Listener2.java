package club.anlan.lanlife.home.listener;

import club.anlan.lanlife.component.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2025/1/2 23:46
 */
@Slf4j
@Component
public class Topic1Listener2 {

    @KafkaListener(topics = {"test_topic2", "test_topic1"}, groupId = "t2g1", clientIdPrefix = "home")
    public void onMessage(ConsumerRecord<String, String> record) {
        log.info("【kafka】, key : {}, value : {}", record.key(), record.value());
        String value = record.value();
        if (StringUtil.isBlank(value)) {
            log.info("kafka获取记录为空");
        }

    }
}
