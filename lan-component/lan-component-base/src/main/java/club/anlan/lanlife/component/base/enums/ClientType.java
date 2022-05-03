package club.anlan.lanlife.component.base.enums;

import club.anlan.lanlife.component.base.i18n.I18nEnum;
import club.anlan.lanlife.component.base.i18n.MessageResource;
import club.anlan.lanlife.component.base.spring.AppContext;
import club.anlan.lanlife.component.utils.StringUtil;

/**
 * 客户端类型
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 20:54
 */
public enum ClientType {

    /**
     * BS客户端
     */
    BS("BS", I18nEnum.CLIENT_TYPE_BS.getKey(), "manager"),

    /**
     * APP
     */
    APP("APP", I18nEnum.CLIENT_TYPE_APP.getKey(), "register"),


    /**
     * @Fields THIRD: 三方系统
     */
    THIRD("THIRD", I18nEnum.CLIENT_TYPE_THIRD.getKey(), "third"),
    ;


    private String code;

    private String name;

    private String type;

    ClientType(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        MessageResource messageSource = AppContext.getBean(MessageResource.class);
        return messageSource.getMessage(name);
    }

    public static ClientType getEnum(String code) {
        if (StringUtil.isEmpty(code)) {
            return null;
        }
        for (ClientType clientType : ClientType.values()) {
            if (StringUtil.equals(clientType.getCode(), code)) {
                return clientType;
            }
        }
        return null;
    }

    /**
     * 返回枚举类型，如果为空则返回 BS
     */
    public static ClientType getEnumDefault(String code) {
        if (StringUtil.isEmpty(code)) {
            return BS;
        }
        for (ClientType clientType : ClientType.values()) {
            if (StringUtil.equals(clientType.getCode(), code)) {
                return clientType;
            }
        }
        return BS;
    }

    public static String getName(String code) {
        String value = null;
        for (ClientType clientType : values()) {
            if (clientType.getCode().equals(code)) {
                value = clientType.getName();
            }
        }
        return value;
    }

    public String getType() {
        return type;
    }

}

