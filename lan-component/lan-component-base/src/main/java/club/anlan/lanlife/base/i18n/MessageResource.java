package club.anlan.lanlife.base.i18n;

/**
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 13:37
 */

import club.anlan.lanlife.base.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component("messageSource")
@Slf4j
public class MessageResource extends MessageResourceExtension {

    /**
     * 国际化地区参数，默认为中国大陆
     */
    private static Locale DEFAULT_LOCALE = Locale.CHINA;

    /**
     * 翻译国际化信息
     *
     * @param code
     * @return
     */
    public String getMessage(String code) {
        if (StringUtils.isEmpty(code)) {
            return StringUtils.EMPTY;
        }
        Locale locale = RequestUtils.getLocale();
        String message = null;
        try {
            message = this.getMessage(code, null, this.getLocale(locale));
            return message;
        }catch (Exception e){
            log.error("exception: {}", e);
        }
        return message;
    }

    /**
     * 翻译国际化信息
     *
     * @param code
     * @param locale
     * @return
     */
    public String getMessage(String code, Locale locale) {
        if (StringUtils.isEmpty(code)) {
            return StringUtils.EMPTY;
        }
        try {
            return this.getMessage(code, null, this.getLocale(locale));
        } catch (Exception e) {
            log.error("exception: {}", e);
        }
        return null;
    }

    /**
     * 翻译国际化信息，并使用参数匹配占位符
     *
     * @param code
     * @param params
     * @return
     */
    public String getMessageWithParams(String code, Object... params) {
        if (StringUtils.isEmpty(code)) {
            return StringUtils.EMPTY;
        }

        Locale locale = RequestUtils.getLocale();
        try {
            return this.getMessage(code, params, this.getLocale(locale));
        } catch (Exception e) {
            log.error("exception: {}", e);
        }
        return null;
    }

    /**
     * 翻译国际化信息，并使用参数匹配占位符
     *
     * @param code
     * @param locale
     * @param params
     * @return
     */
    public String getMessageWithParams(String code, Locale locale, Object... params) {
        if (StringUtils.isEmpty(code)) {
            return StringUtils.EMPTY;
        }
        try {
            return this.getMessage(code, params, this.getLocale(locale));
        } catch (Exception e) {
            log.error("exception: {}", e);
        }
        return null;
    }

    /**
     * 语言如果为空，设置默认语言
     */
    private Locale getLocale(Locale locale) {
        // logger.info("locale: {}", locale);
        Locale result = locale;
        if (result == null || StringUtils.isEmpty(result.getLanguage())) {
            result = DEFAULT_LOCALE;
        }
        return result;
    }
}
