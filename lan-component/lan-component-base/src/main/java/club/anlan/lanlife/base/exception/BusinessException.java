package club.anlan.lanlife.base.exception;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 14:22
 */
public class BusinessException extends BasicException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String i18nCode) {
        super(i18nCode);
    }

    public BusinessException(String i18nCode, Object... params) {
        super(i18nCode, params);
    }

    public BusinessException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }

    public BusinessException(String code, String errMsg) {
        super(code, errMsg);
    }

    public BusinessException(String code, String errMsg, Throwable cause) {
        super(code, errMsg, cause);
    }
}

