package club.anlan.lanlife.component.redis.cache;

import club.anlan.lanlife.component.base.enums.ClientType;
import club.anlan.lanlife.component.base.enums.RoleType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * UserSessionInfo
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 21:37
 */
@Data
public class UserSessionInfo implements Serializable {

    private static final long serialVersionUID = -6058962506463021785L;

    /**
     * accessToken
     */
    private String token;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录用户名
     */
    private String loginName;

    /**
     * 客户端类型
     */
    private ClientType clientType = ClientType.BS;

    /**
     * 地区
     */
    private Locale locale = Locale.CHINA;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 上次刷新缓存时间
     */
    private Long lastRefreshTime;
}
