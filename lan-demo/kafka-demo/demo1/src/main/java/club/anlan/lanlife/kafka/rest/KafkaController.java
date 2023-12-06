package club.anlan.lanlife.kafka.rest;

import club.anlan.lanlife.component.base.result.ResultMessage;
import club.anlan.lanlife.kafka.producer.KafkaSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * kafka moct test controller
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/5 17:07
 */
@RestController
@RequestMapping("/demo1/kafka")
public class KafkaController {

    @Resource
    private KafkaSender kafkaSender;

    @PostMapping(value = "/send")
    public ResultMessage send(){
        kafkaSender.sendTestMsg("test");
        return ResultMessage.createSuccessResult();
    }
}
