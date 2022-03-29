package club.anlan.lanlife.component.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/5 20:36
 */
@Configuration
@Slf4j
public class BaseConfig implements EnvironmentAware {


    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    /**
     * 获取配置项前缀，子类可根据需求进行配置，默认不设置前缀
     *
     * @return
     */
    public String getConfigPrefix() {
        return "";
    }

    public String getProperty(String key) {
        String completeKey = getConfigPrefix() + key;
        return env.getProperty(completeKey);
    }

    public String getProperty(String key, String defaultValue) {
        String completeKey = getConfigPrefix() + key;
        return env.getProperty(completeKey, defaultValue);
    }

    public int getPropertyInt(String key) {
        String completeKey = getConfigPrefix() + key;
        try {
            return Integer.parseInt(Objects.requireNonNull(env.getProperty(completeKey)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    public long getPropertyLong(String key){
        String completeKey = getConfigPrefix() + key;
        try {
            return Long.parseLong(Objects.requireNonNull(env.getProperty(completeKey)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    public boolean getPropertyBoolean(String key){
        String completeKey = getConfigPrefix() + key;
        try {
            return Boolean.parseBoolean(env.getProperty(completeKey));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
