package club.anlan.lanlife.component.kafka.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class KafkaProducer {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMsg(String topic, Object data) {
        String str = null;
        if (data instanceof String) {
            str = data.toString();
        } else {
            str = JSON.toJSONString(data);
        }
        log.info("Topic【{}】 {}", topic, str);
        kafkaTemplate.send(topic, str);
    }

}
