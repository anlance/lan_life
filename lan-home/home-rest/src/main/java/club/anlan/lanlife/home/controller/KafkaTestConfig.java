package club.anlan.lanlife.home.controller;

import club.anlan.lanlife.component.kafka.KafkaConfig;
import club.anlan.lanlife.component.kafka.producer.listener.ProducerLogListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2024/12/27 17:11
 */
@Configuration
public class KafkaTestConfig {

    @Resource
    private KafkaConfig kafkaConfig;

    @Resource
    private ProducerLogListener producerLogListener;

    @PostConstruct
    @Bean("otherKafkaTemplate")
    public KafkaTemplate<String, String> otherKafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(otherProducerFactory());
        kafkaTemplate.setProducerListener(producerLogListener);
        return kafkaTemplate;
    }

    @Bean("otherKafkaTemplate2")
    public KafkaTemplate<String, String> otherKafkaTemplate2() {
        return new KafkaTemplate<String, String>(otherProducerFactory2());
    }

    @Bean("otherKafkaTemplate3")
    public KafkaTemplate<String, String> otherKafkaTemplate3() {
        return new KafkaTemplate<String, String>(otherProducerFactory3());
    }

    @Bean("otherKafkaTemplate4")
    public KafkaTemplate<String, String> otherKafkaTemplate4() {
        return new KafkaTemplate<String, String>(otherProducerFactory4());
    }

    @Bean("otherProducerFactory")
    public ProducerFactory<String, String> otherProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaConfig.producerConfigs());
    }

    @Bean("otherProducerFactory2")
    public ProducerFactory<String, String> otherProducerFactory2() {
        return new DefaultKafkaProducerFactory<>(kafkaConfig.producerConfigs());
    }

    @Bean("otherProducerFactory3")
    public ProducerFactory<String, String> otherProducerFactory3() {
        return new DefaultKafkaProducerFactory<>(kafkaConfig.producerConfigs());
    }

    @Bean("otherProducerFactory4")
    public ProducerFactory<String, String> otherProducerFactory4() {
        return new DefaultKafkaProducerFactory<>(kafkaConfig.producerConfigs());
    }



    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(Integer.MAX_VALUE-1);
        int i = atomicInteger.incrementAndGet();
        System.out.println(i);
        System.out.println(1 << 3);
        System.out.println(1 << 4);
        System.out.println(1 << 1);
        System.out.println(1 << 0);
    }
}
