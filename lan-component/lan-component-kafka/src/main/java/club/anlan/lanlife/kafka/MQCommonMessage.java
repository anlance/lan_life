/**
 * *********************** 版权声明 ***********************************
 *
 * 版权所有：浙江大华技术股份有限公司
 * ©CopyRight DahuaTech 2020   
 *  
 * *******************************************************************
*/
package club.anlan.lanlife.kafka;

import org.springframework.util.StringUtils;


public class MQCommonMessage<T> {

	public static final String DEFALUT_MQ_CONTENT_TYPE = "json";

	/**
	 * @Fields id: 消息id, 请求需要回复时可以用到
	 */
	private String id;

	/**
	 * @Fields source: 消息来源的服务名
	 */
	private String source;

	/**
	 * @Fields timestamp: 时间戳
	 */
	private Long timestamp;

	/**
	 * @Fields type: 消息类型枚举
	 */
	private String type;
	
	/**
	 * @Fields contentType: 消息格式,默认json
	 */
	private String contentType;

	/**
	 * @Fields body: 消息体
	 */
	private T body;

	public MQCommonMessage() {
		super();
	}

	public MQCommonMessage(String source, String type, T body) {
		super();
		this.source = source;
		this.type = type;
		this.body = body;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getTimestamp() {
		if (timestamp == null) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContentType() {
		if (StringUtils.isEmpty(contentType)) {
			return DEFALUT_MQ_CONTENT_TYPE;
		}
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
}
