package club.anlan.lanlife.demo.init;

import club.anlan.lanlife.demo.domain.TestKafkaParam;
import club.anlan.lanlife.demo.kafka.consumer.TestConsumerConsumer;
import club.anlan.lanlife.demo.kafka.producer.TestSecondProducer;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * kafka测试初始化
 *
 * @author lan
 * @version 1.0
 * @date 2021/8/15 22:18
 */
@Component
public class TestKafkaInit {

    @Autowired
    private TestSecondProducer testSecondProducer;

    @Autowired
    private TestConsumerConsumer testConsumerConsumer;

    @PostConstruct
    void init() {
        initAndSendData();
    }

    private void initAndSendData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                        index += 1;
                        testSecondProducer.sendDataCenterSubCode(JSONObject.toJSONString(new TestKafkaParam(index, new Date())));
                        testSecondProducer.sendDataCenterSubCode2(JSONObject.toJSONString(new TestKafkaParam(index, new Date())));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (index >= 10000) {
                        index = 0;
                    }
                }
            }
        }).start();
    }
}
