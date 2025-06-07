package club.anlan.lanlife.home.controller;

import club.anlan.lanlife.component.base.annotation.Log;
import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.home.client.BasicClient;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试类
 *
 * @author lan
 * @version 1.0
 * @date 2023/12/24 16:39
 */
@RestController
@Slf4j
@RequestMapping("/template")
public class TemplateController {

    public static final String TEST_TOPIC = "test_topic";
    public static final String TEST_TOPIC2 = "test_topic2";
    public static final String TEST_TOPIC3 = "test_topic3";
    public static final String TEST_TOPIC4 = "test_topic4";
    public static final String TEST_TOPIC5 = "test_topic5";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    @Qualifier("otherKafkaTemplate")
    private KafkaTemplate<String, String> otherKafkaTemplate;

    @Resource
    @Qualifier("otherKafkaTemplate2")
    private KafkaTemplate<String, String> otherKafkaTemplate2;

    @Resource
    @Qualifier("otherKafkaTemplate3")
    private KafkaTemplate<String, String> otherKafkaTemplate3;

    @Resource
    @Qualifier("otherKafkaTemplate4")
    private KafkaTemplate<String, String> otherKafkaTemplate4;

    @Resource
    private BasicClient basicClient;

    //@PostConstruct
    //public static void initFlowQpsRule() {
    //    List<FlowRule> rules = new ArrayList<>();
    //    FlowRule rule1 = new FlowRule();
    //    rule1.setResource("sayHello");
    //    // Set max qps to 20
    //    rule1.setCount(5);
    //    rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
    //    rule1.setLimitApp("default");
    //    rules.add(rule1);
    //    FlowRuleManager.loadRules(rules);
    //}

    @Log(description = "等待N秒后返回")
    @GetMapping("/wait")
    public ResultMessage wait(@RequestParam(value = "second", required = false) int second) {
        try {
            basicClient.getSalt("faker");
            Thread.sleep(second);
        } catch (Exception e) {
            log.error("sleep error, ", e);
        }
        return ResultMessage.createSuccessResult(second);
    }

    @Log(description = "for循环请求basic服务")
    @GetMapping("/callBasic")
    public ResultMessage callBasic(@RequestParam(value = "second", required = false) int second) {
        while (true) {
            for (int i = 0; i < second; i++) {
                basicClient.getSalt("faker");
            }
            try {
                Thread.sleep(60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Log(description = "生成对象")
    @GetMapping("/allocate")
    public ResultMessage allocate(@RequestParam(value = "second", required = false) int second) {
        while (true) {
            for(int i=0;i<second;i++){
                Long[] longs = new Long[1024 * 1024 * 10];
                for(int k=0;k<1000;k++){
                    longs[k] = Long.valueOf(k);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Log(description = "test kafka")
    @GetMapping("/kafka")
    //@SentinelResource(value = "sayHello", blockHandler = "blockHandlerForGetUser")
    public ResultMessage kafka() {
        //sendMsg(TEST_TOPIC,"test1");
        otherSendMsg(TEST_TOPIC2, "test1");
        otherSendMsg(TEST_TOPIC2, "test1");
        //otherSendMsg2(TEST_TOPIC3,"test1");
        //otherSendMsg3(TEST_TOPIC4,"test1");
        //otherSendMsg4(TEST_TOPIC5,"test1");
        return ResultMessage.createSuccessResult();
    }

    public ResultMessage blockHandlerForGetUser(String id, BlockException ex) {
        return ResultMessage.createSuccessResult("false");
    }

    //@Log(description = "test delete")
    @DeleteMapping("/delete")
    public ResultMessage delete(String ids) {
        log.info("dedddddddddd {}", ids);
        return ResultMessage.createSuccessResult();
    }


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

    public void otherSendMsg(String topic, Object data) {
        String str = null;
        if (data instanceof String) {
            str = data.toString();
        } else {
            str = JSON.toJSONString(data);
        }
        log.info("Topic【{}】 {}", topic, str);
        otherKafkaTemplate.send(topic, str);
    }

    public void otherSendMsg2(String topic, Object data) {
        String str = null;
        if (data instanceof String) {
            str = data.toString();
        } else {
            str = JSON.toJSONString(data);
        }
        log.info("Topic【{}】 {}", topic, str);
        otherKafkaTemplate2.send(topic, str);
    }

    public void otherSendMsg3(String topic, Object data) {
        String str = null;
        if (data instanceof String) {
            str = data.toString();
        } else {
            str = JSON.toJSONString(data);
        }
        log.info("Topic【{}】 {}", topic, str);
        otherKafkaTemplate3.send(topic, str);
    }

    public void otherSendMsg4(String topic, Object data) {
        String str = null;
        if (data instanceof String) {
            str = data.toString();
        } else {
            str = JSON.toJSONString(data);
        }
        log.info("Topic【{}】 {}", topic, str);
        otherKafkaTemplate4.send(topic, str);
    }


}
