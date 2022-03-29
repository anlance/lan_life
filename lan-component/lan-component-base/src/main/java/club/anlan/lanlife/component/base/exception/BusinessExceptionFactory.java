package club.anlan.lanlife.component.base.exception;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 14:22
 */
public class BusinessExceptionFactory {

    public static BusinessException createBisException(String i18nCode) {
        return new BusinessException(i18nCode);
    }

    public static BusinessException createBisException(String code, String errMsg) {
        return new BusinessException(code, errMsg);
    }

    public static BusinessException createBisException(String code, String errMsg, Throwable cause) {
        return new BusinessException(code, errMsg);
    }

    public static BusinessException createCommonBisException(String errMsg) {
        return new BusinessException(errMsg);
    }

    public static BusinessException createCommonBisException(String code, String errMsg) {
        return new BusinessException(code, errMsg);
    }
}
