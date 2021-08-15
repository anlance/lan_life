/**
 * *********************** 版权声明 ***********************************
 *
 * 版权所有：浙江大华技术股份有限公司
 * ©CopyRight DahuaTech 2019   
 *  
 * *******************************************************************
*/
package club.anlan.lanlife.kafka.producer;

import org.springframework.stereotype.Component;


@Component
public class OperateLogProducer extends KafkaProducer {

	public static final String OPERATE_LOG_QUEUE = "ALL_TO_BASIC_OPERATELOG";


	public void sendMsg(Object data) {
		super.sendMsg(OPERATE_LOG_QUEUE, data);
	}
}
