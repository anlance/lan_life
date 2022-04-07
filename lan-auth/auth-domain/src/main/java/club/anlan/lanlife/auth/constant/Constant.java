package club.anlan.lanlife.auth.constant;

/**
 * 常量
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 15:11
 */
public class Constant {

    /**
     * @Fields ERROR_INVALID_GRANT: 用户名或密码错误
     */
    public static final String ERROR_INVALID_GRANT = "invalid_grant";


    public static final String ERROR_BAD_CREDENTIALS = "Bad credentials";

    public static final String ERROR_INVALID_SCOPE = "invalid_scope";

    public static final String ERROR_UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";


    public static final String SYSTEM_ERROR_PARAMETER_VALIDATE = "system.parameter.validate.error";


    /**
     * @Fields ERROR_USERNAME_ERROR: 用户名错误
     */
    public static final String ERROR_USERNAME_ERROR = "error.username.error";

    /**
     * @Fields SYSTEM_ERROR_DEFAULT: 未知错误
     */
    public static final String SYSTEM_ERROR_DEFAULT = "system.error";

    /**
     * @Fields SYSTEM_ERROR_INVALID: 登录已失效
     */
    public static final String SYSTEM_ERROR_INVALID = "system.error.invalid";

    /**
     * @Fields ERROR_LOGIN_FAILED: 登录失败
     */
    public static final String ERROR_LOGIN_FAILED = "error.login.failed";

    /**
     * @Fields ERROR_USERNAME_OR_PASSWORD: 用户名或密码错误
     */
    public static final String ERROR_USERNAME_OR_PASSWORD = "error.username.or.password";

    /**
     * @Fields ERROR_ACCOUNT_IS_LOCKED: 账户已被冻结,请{0}秒以后再试
     */
    public static final String ERROR_ACCOUNT_IS_LOCKED = "error.account.is.locked";

    /**
     * @Fields ERROR_USERNAME_OR_PASSWORD_EXT: 用户名或密码错误(需要参数校验)
     */
    public static final String ERROR_USERNAME_OR_PASSWORD_EXT = "error.username.or.password.ext";

    /**
     * @Fields ERROR_PHONE_NUMBER_EMPTY: 手机号不能为空
     */
    public static final String ERROR_PHONE_NUMBER_EMPTY = "error.phone.number.empty";

    /**
     * @Fields ERROR_VERIFICATION_CODE: 验证码错误
     */
    public static final String ERROR_VERIFICATION_CODE = "error.verification.code";

    /**
     * @Fields ERROR_USERNAME_OR_PASSWORD_EXT: 用户名或密码错误(需要短信验证码)
     */
    public static final String ERROR_USERNAME_OR_PASSWORD_SMS = "error.username.or.password.sms";

    /**
     * @Fields ERROR_ACCOUNT_BEEN_FROZEN: 账号已被禁用
     */
    public static final String ERROR_ACCOUNT_BEEN_FROZEN = "error.account.been.frozen";

    /**
     * @Fields ERROR_AUTO_LOGIN_CLOSEED: 未开启自动登录
     */
    public static final String ERROR_AUTO_LOGIN_CLOSEED = "error.auto.login.closed";

    /**
     * @Fields ERROR_SMS_SEND_FAILED: 短信发送失败
     */
    public static final String ERROR_SMS_SEND_FAILED = "error.sms.send.failed";

    /**
     * @Fields ERROR_SMS_SEND_OVER: 短信接收次数已达上限
     */
    public static final String ERROR_SMS_SEND_OVER = "error.sms.send.over";

    /**
     * @Fields MSG_LOG_LOGIN: 登录
     */
    public static final String MSG_LOG_LOGIN = "msg.log.login";

    /**
     * @Fields MSG_LOG_LOGOUT: 登出
     */
    public static final String MSG_LOG_LOGOUT = "msg.log.logout";

    /**
     * @Fields MSG_LOG_RESET_PASSWORD: 重置密码
     */
    public static final String MSG_LOG_RESET_PASSWORD = "msg.log.reset.password";

    /**
     * @Fields ERROR_USERNAME_OR_PASSWORD_EXT: 用户名或密码错误,还剩{0}次机会
     */
    public static final String ERROR_USERNAME_OR_PASSWORD_TIP = "error.username.or.password.tip";

    /**
     * @Fields ERROR_PHONE_NUMBER_ERROR: 手机号不正确
     */
    public static final String ERROR_PHONE_NUMBER_ERROR = "error.phone.number.error";

    /**
     * @Fields ERROR_CONFIGURATION_MISSING: 配置缺失
     */
    public static final String ERROR_CONFIGURATION_MISSING = "error.configuration.missing";

    public static final String DOUBLE_UNDERLINE = "__";
}
