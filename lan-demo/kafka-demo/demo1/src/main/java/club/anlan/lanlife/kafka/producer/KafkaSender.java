package club.anlan.lanlife.kafka.producer;

import com.alibaba.fastjson.JSON;
import club.anlan.lanlife.kafka.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * kafka sender
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/5 17:11
 */
@Component
@Slf4j
public class KafkaSender {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostConstruct
    public void sendTest(){
        sendTestMsg("test");
    }

    @PostConstruct
    public void sendTest2(){
        sendMsg("test1","test");
    }

    @PostConstruct
    public void sendTest3(){
        sendMsg("test2","test");
    }

    public void sendTestMsg(Object data) {
        sendMsg(KafkaTopics.TEST_TOPIC, data);
    }

    public void sendMsg(String topic, Object data) {
        String str = null;
        if (data instanceof String) {
            str = String.valueOf(data);
        } else {
            str = JSON.toJSONString(data);
        }
        log.info("Topic【{}】 {}", topic, str);
        kafkaTemplate.send(topic, str);
    }


}
