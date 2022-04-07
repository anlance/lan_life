package club.anlan.lanlife.component.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 13:45
 */
@Slf4j
public class RequestUtils {

    /**
     * 国际化地区参数，默认为中国大陆
     */
    private static Locale DEFAULT_LOCALE = Locale.CHINA;

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null && attributes.getRequest() != null) {
            HttpServletRequest request = attributes.getRequest();
            return request;
        }

        return null;
    }

    /**
     * 获取Locale信息--国家、地区
     *
     * @return
     */
    public static Locale getLocale(){
        Locale locale = null;
        HttpServletRequest request = getRequest();
        if(request != null){
            locale = request.getLocale();
        }
        log.info("locale: {}", locale);
        if (locale == null || StringUtils.isEmpty(locale.getLanguage())) {
            locale = DEFAULT_LOCALE;
        }
        return locale;
    }

    /**
     *
     * @param sourceIp 源ip
     *
     * @return 41位的全局唯一的请求标识
     */
    public static String generateRequestId(String sourceIp) {
        // 获取ip对应的小写十六进制字符串
        String hexString = IPUtil.convert2Hex(sourceIp, true, 8);

        // 生成不包含"-"的32位UUID字符串
        String uuidString = UUID.randomUUID().toString().replace("-", "").toLowerCase();

        return hexString + "-" + uuidString;
    }
}

