/**
 * *********************** 版权声明 ***********************************
 *
 * 版权所有：浙江大华技术股份有限公司
 * ©CopyRight DahuaTech 2019   
 *  
 * *******************************************************************
*/
package club.anlan.lanlife.kafka.producer;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducer {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMsg(String topic, Object data) {
		String str = null;
		if (data instanceof String) {
			str = data.toString();
		} else {
			str = JSON.toJSONString(data);
		}
		logger.info("Topic【{}】 {}", topic, str);
		kafkaTemplate.send(topic, str);
	}

}
