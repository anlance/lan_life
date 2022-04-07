package club.anlan.lanlife.component.base.util;

import club.anlan.lanlife.component.base.enums.ApiResponseCodeEnum;
import club.anlan.lanlife.component.base.exception.BusinessRuntimeException;
import club.anlan.lanlife.component.base.exception.NullException;
import club.anlan.lanlife.component.base.exception.ServiceException;
import club.anlan.lanlife.component.base.i18n.I18n;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.component.utils.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 断言工具
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 15:38
 */
@Slf4j
public final class AssertUtil {

    private AssertUtil() {
    }

    public static void notNull(Object object, String template, Object... params) {
        if (null == object) {
            // 单独做params为null判断是为了防止params=null时template中含有占位字符导致StringUtil.formatByRegex
            // 方法抛MissingFormatArgumentException异常
            String msg = null == params ? template : StringUtil.formatByRegex(template, params);
            throw new NullException(msg);
        }
    }

    public static void notNull(Object object, I18n i18n, Object... params) {
        if (null == object) {
            throw BusinessRuntimeException.createI18nException(ApiResponseCodeEnum.BUSINESS_ERROR, i18n, params);
        }
    }

    public static void notNull(Object object, ApiResponseCodeEnum codeEnum, I18n i18n, Object... params) {
        if (null == object) {
            throw BusinessRuntimeException.createI18nException(codeEnum, i18n, params);
        }
    }


    public static void isNull(Object object, I18n i18n, Object... params) {
        if (null != object) {
            throw BusinessRuntimeException.createI18nException(ApiResponseCodeEnum.BUSINESS_ERROR, i18n, params);
        }
    }

    public static void isNull(Object object, ApiResponseCodeEnum codeEnum, I18n i18n, Object... params) {
        if (null != object) {
            throw BusinessRuntimeException.createI18nException(codeEnum, i18n, params);
        }
    }

    public static void check(final boolean expression, I18n i18n, Object... params) {
        if (!expression) {
            throw BusinessRuntimeException.createI18nException(ApiResponseCodeEnum.BUSINESS_ERROR, i18n, params);
        }
    }

    public static void check(final boolean expression, ApiResponseCodeEnum code, I18n i18n, Object... params) {
        if (!expression) {
            throw BusinessRuntimeException.createI18nException(code, i18n, params);
        }
    }

    public static void isTrue(Boolean boo, String template, Object... params) {
        if (boo) {
            String message = StringUtil.formatByRegex(template, params);
            throw new ServiceException(message);
        }
    }

    public static void notTrue(Boolean boo, String template, Object... params) {
        if (!boo) {
            String message = StringUtil.formatByRegex(template, params);
            throw new ServiceException(message);
        }
    }

    private static String getLogMessage(String msg) {
        String[] splits = msg.split("::");
        return splits.length > 1 ? splits[1] : splits[0];
    }

    private static String getShowMessage(String msg) {
        String[] splits = msg.split("::");
        return splits[0];
    }
}
