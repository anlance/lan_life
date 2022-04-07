package club.anlan.lanlife.component.base.enums;

import club.anlan.lanlife.component.base.i18n.I18nEnum;
import club.anlan.lanlife.component.base.i18n.MessageResource;
import club.anlan.lanlife.component.base.spring.AppContext;

/**
 * 用户类型
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 22:15
 */
public enum RoleType {

    /**
     * 管理员
     */
    MANAGER(1, I18nEnum.USER_TYPE_MANAGER.getKey()),

    /**
     * APP: APP用户
     */
    APP(2, I18nEnum.USER_TYPE_APP.getKey()),
    ;

    private Integer code;

    private String name;

    RoleType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        MessageResource messageSource = AppContext.getBean(MessageResource.class);
        return messageSource.getMessage(name);
    }

    public static String getName(Integer code) {
        String value = null;
        for (RoleType clientType : values()) {
            if (clientType.getCode().equals(code)) {
                value = clientType.getName();
            }
        }
        return value;
    }

    public static RoleType getEnum(Integer code) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getCode().equals(code)) {
                return roleType;
            }
        }
        return null;
    }
}

