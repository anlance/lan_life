package club.anlan.lanlife.demo.kafka.producer;

import club.anlan.lanlife.demo.kafka.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 测试消息发送者
 *
 * @author lan
 * @version 1.0
 * @date 2021/8/15 22:08
 */
@Component
@Slf4j
public class TestSecondProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送测试消息
     *
     * @param data 消息数据
     */
    public void sendDataCenterSubCode(String data) {
        log.info("【发送】-【{}】： {}", KafkaConstants.TEST_SECOND_TOPIC, data);
        kafkaTemplate.send(KafkaConstants.TEST_SECOND_TOPIC, data);
    }

    public void sendDataCenterSubCode2(String data) {
        log.info("【发送】-【{}】： {}", KafkaConstants.TEST_SECOND2222_TOPIC, data);
        kafkaTemplate.send(KafkaConstants.TEST_SECOND2222_TOPIC, data);
    }
}
