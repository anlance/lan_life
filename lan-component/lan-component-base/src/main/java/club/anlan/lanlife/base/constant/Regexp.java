package club.anlan.lanlife.base.constant;

/**
 * 正则表达式常量类
 *
 * @author    43240
 * @date      2018年12月26日
 */
public class Regexp {
	//IP
    public final static String REG_EXP_IP = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    //账户名
    public final static String USERNAME = "(?!^(\\d+|[a-zA-Z]+|[@._]+)$)^[\\w@._]{5,25}$"; 
    //或
    public static final String OR = "|";
    //空
    public static final String EMPTY = "(^$)";
    //数字、字母、下划线、中文
    public static final String NAME = "^[a-zA-Z0-9_\\u4E00-\\u9FA5]+$";
    //设备名称
    public static final String DEVICE_NAME = "^[a-zA-Z0-9\\-_@\\u4E00-\\u9FA5]{0,25}$";
    //手机号
    public static final String PHONE = "0?(13|14|15|16|17|18|19)[0-9]{9}";
    //电话号
    public static final String TELEPHONE = "^(([0\\+][0-9]{2,3}-)?(0[0-9]{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$";
    //手机号或电话号或空
    public static final String TEL_OR_PHONE_OR_EMPTY = TELEPHONE + OR + PHONE + OR + EMPTY;
    //手机号或电话号
    public static final String TEL_OR_PHONE = TELEPHONE + OR + PHONE;
    //手机号或空
    public static final String PHONE_OR_EMPTY = PHONE + OR + EMPTY;
    //性别
    public static final String SEX = "^['男'|'女']$";
    //时间段
    public static final String TIME_PEROID = "^([0-9]{2}:[0-9]{2}:[0-9]{2}~[0-9]{2}:[0-9]{2}:[0-9]{2})$";
    //电子邮箱
    public final static String EMAIL = "^([a-zA-Z0-9_\\.\\-\\+])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})$";
    //门店或者组织名称
    public static final String STORE_NAME = "^[a-zA-Z0-9\\-_@#()\\u4E00-\\u9FA5]{1,25}$";
    //门店面积
    public static final String STORE_AREA = "^(\\d{1,6}(\\.\\d{1,2})?|100000)$"; 
    //经纬度
    public static final String STORE_LNG_LAT = "^[-\\+]?[.\\d]*$"; 
    //表情符号
    public static final String HAS_EMOJI = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
    
	/**
	 * @Fields ERROR_CODE_PATTERN: 错误码格式
	 */
	public static final String ERROR_CODE_PATTERN = "^[A-Z]{2}[0-9]{4}$";

}