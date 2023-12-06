package club.anlan.lanlife.component.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/16 22:29
 */
@Slf4j
@Component("logKafkaListenerErrorHandler")
public class LogKafkaListenerErrorHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) throws Exception {
        log.info("msg: {}", JSON.toJSONString(message));
        log.error("exception: ", exception);
        return null;
    }

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) throws Exception {
        log.info("msg: {}", JSON.toJSONString(message));
        log.error("exception: ", exception);
        return KafkaListenerErrorHandler.super.handleError(message, exception, consumer);
    }
}
