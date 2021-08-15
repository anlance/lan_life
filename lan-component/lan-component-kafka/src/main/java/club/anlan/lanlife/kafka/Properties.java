/**
 * *********************** 版权声明 ***********************************
 *
 * 版权所有：浙江大华技术股份有限公司
 * ©CopyRight DahuaTech 2019   
 *  
 * *******************************************************************
*/
package club.anlan.lanlife.kafka;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Data
public class Properties {

	@Value("${kafka.consumer.servers}")
	private String servers;

	@Value("${kafka.consumer.group-id}")
	private String groupId;

	@Value("${kafka.consumer.enable-auto-commit:true}")
	private String enableAutoCommit;

	@Value("${kafka.consumer.session-timeout:15000}")
	private String sessionTimeout;

	@Value("${kafka.consumer.auto-commit-interval:100}")
	private String autoCommitInterval;

	@Value("${kafka.consumer.auto-offset-reset:earliest}")
	private String autoOffsetReset;

	@Value("${kafka.producer.retries-config:1}")
	private String retriesConfig;

	@Value("${kafka.producer.batch-size-config:16384}")
	private String batchSizeConfig;

	@Value("${kafka.producer.linger-ms-config:1}")
	private String lingerMsConfig;

	@Value("${kafka.producer.buffer-memory-config:1024000}")
	private String bufferMemoryConfig;

}
