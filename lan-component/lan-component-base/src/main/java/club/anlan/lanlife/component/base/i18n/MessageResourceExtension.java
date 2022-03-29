package club.anlan.lanlife.component.base.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.annotation.PostConstruct;

/**
 * 加载资源异常
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 13:39
 */
@Slf4j
public class MessageResourceExtension extends ReloadableResourceBundleMessageSource {



    /**
     * cache-seconds
     */
    @Value(value = "${i18n.messages.cache-seconds:-1}")
    private int cacheSeconds;

    /**
     * encoding
     */
    @Value(value = "${i18n.messages.encoding:UTF-8}")
    private String encoding;

    /**
     * basename
     */
    @Value(value = "${i18n.messages.basename:messageResource}")
    private String basename;

    /**
     * baseFolder
     */
    @Value(value = "${i18n.messages.baseFolder:i18n}")
    private String baseFolder;


    @PostConstruct
    public void init() {
        logger.info("init MessageResourceExtension ...");
        String[] baseNames = new String[] { "classpath:" + baseFolder + "/" + basename,
                "classpath:i18n/commonentMessages" };
        this.setCacheSeconds(cacheSeconds);
        this.setDefaultEncoding(encoding);
        this.setBasenames(baseNames);
    }


}
