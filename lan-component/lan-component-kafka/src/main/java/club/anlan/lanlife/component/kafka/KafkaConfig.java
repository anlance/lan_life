package club.anlan.lanlife.component.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
public class KafkaConfig {

    @Autowired
    private Properties kafkaProperties;

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }


    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }


    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        // kafka服务地址
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServers());
        // 消费者群组ID
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId());
        // 是否自动提交
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getEnableAutoCommit());
        // 自动提交的频率
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaProperties.getAutoCommitInterval());
        // 连接超时时间
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProperties.getSessionTimeout());
        // 设置消费方式
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getAutoOffsetReset());
        // 键的序列化方式
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 值的序列化方式
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return propsMap;
    }


    public Map<String, Object> producerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        // kafka服务地址
        propsMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServers());
        // 重试机制，0为不启用重试机制
        propsMap.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getRetriesConfig());
        // 控制批处理大小，单位字节
        propsMap.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getBatchSizeConfig());
        // 批量发送，延迟1毫秒，启用该功能能有效减少生产者发送消息次数，从而提交并发量
        propsMap.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getLingerMsConfig());
        // 生产者可以使用总内存字节，来缓冲等待发送到服务器的记录
        propsMap.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.getBufferMemoryConfig());
        // 键的序列化方式
        propsMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 值的序列化方式
        propsMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        propsMap.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, Collections.singletonList(MdcProducerInterceptor.class));
        return propsMap;
    }

}
