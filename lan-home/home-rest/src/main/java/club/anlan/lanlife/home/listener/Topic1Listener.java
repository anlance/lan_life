package club.anlan.lanlife.home.listener;

import club.anlan.lanlife.component.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2025/1/2 23:46
 */
@Slf4j
@Component
public class Topic1Listener {

    //@KafkaListener(topics = "test_topic2", clientIdPrefix = "test-faker")
    public void onMessage(ConsumerRecord<String, String> record) {
        log.info("【kafka】, key : {}, value : {}", record.key(), record.value());
        String value = record.value();
        if (StringUtil.isBlank(value)) {
            log.info("kafka获取记录为空");
        }

    }

    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(int num: nums){
            map.put(num, map.getOrDefault(num,0)+1);
        }

        PriorityQueue<Map.Entry<Integer,Integer>> queue = new PriorityQueue(k, new Comparator<Map.Entry<Integer,Integer>>() {
            @Override
            public int compare(Map.Entry<Integer,Integer> o1, Map.Entry<Integer,Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        });
        for(Map.Entry<Integer,Integer> entry: map.entrySet()){
            queue.add(entry);
        }
        int[] res = new int[k];
        for (int i=0;i<k;i++){
            res[i] = queue.poll().getKey();
        }
        return res;
    }
}
