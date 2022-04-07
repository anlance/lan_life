package club.anlan.lanlife.component.storage.file;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件处理工厂
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/12 22:28
 */
@Component
public class FileHandlerFactory implements ApplicationContextAware, InitializingBean {

    private List<FileHandler> handlers = new ArrayList<FileHandler>();

    private ApplicationContext applicationContext;

    @Value("${storage.type:fastdfs}")
    private String storageType;

    private void registerFileHandler(FileHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, FileHandler> handlers = applicationContext.getBeansOfType(FileHandler.class);
        for (Map.Entry<String, FileHandler> entry : handlers.entrySet()) {
            registerFileHandler(entry.getValue());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    public FileHandler getFileHandler(String storageType) {
        for (FileHandler handler : handlers) {
            if (StringUtils.equals(storageType, handler.getHandlerType())) {
                return handler;
            }
        }
        return null;
    }

    public FileHandler getFileHandler() {
        if (StringUtils.isNotEmpty(storageType)) {
            for (FileHandler handler : handlers) {
                if (StringUtils.equals(storageType, handler.getHandlerType())) {
                    return handler;
                }
            }
        }
        return null;
    }
}
