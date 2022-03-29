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
