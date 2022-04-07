package club.anlan.lanlife.component.base.enums;

/**
 * api接口返回码枚举
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 0:31
 */
public enum ApiResponseCodeEnum {

    SUCCESS("0000", "请求成功"),

    SYSTEM_ERROR("0001", "系统错误"),

    SERVICE_ERROR("00002", "服务错误"),

    BUSINESS_ERROR("00003", "业务异常"),

    PARAMETER_ERROR("00004", "参数校验失败"),
    ;

    private final String value;
    private final String name;

    ApiResponseCodeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
