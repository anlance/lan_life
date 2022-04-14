package club.anlan.lanlife.component.base.i18n;

import lombok.Getter;

/**
 * i18n统一key
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/29 0:37
 */
public enum I18nEnum implements I18n {

    SYSTEM_ERROR("system.error", "系统处理失败"),
    ERROR_PARAMETER_VALIDATE("error.parameter.validate", "参数校验不通过"),
    ERROR_PARAMETER_TYPE("error.parameter.type", "参数类型不正确"),
    CLIENT_TYPE_BS("client.type.bs", "BS客户端"),
    CLIENT_TYPE_APP("client.type.app", "CS客户端 "),
    USER_TYPE_MANAGER("user.type.manager", "管理员用户"),
    USER_TYPE_APP("user.type.app", "APP用户"),
    ERROR_USERNAME_OR_PASSWORD("error.username.or.password", "用户名或密码错误"),

    ;

    @Getter
    private final String defaultMessage;
    private final String key;

    I18nEnum(String key, String defaultMessage) {
        this.defaultMessage = defaultMessage;
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
