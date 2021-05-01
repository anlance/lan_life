package club.anlan.lanlife.storage.image;

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
 * 图片处理工厂
 *
 * @author lan
 * @version 1.0
 * @date 2021/3/28 11:59
 */
@Component
public class ImageHandlerFactory implements ApplicationContextAware, InitializingBean {

    private List<ImageHandler> handlers = new ArrayList<ImageHandler>();

    private ApplicationContext applicationContext;

    @Value("${storage.type:fastdfs}")
    private String imageType;

    private void regisiterImageHandler(ImageHandler handler){
        handlers.add(handler);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ImageHandler> handlers = applicationContext.getBeansOfType(ImageHandler.class);
        for (Map.Entry<String,ImageHandler> entry : handlers.entrySet()) {
            regisiterImageHandler(entry.getValue());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ImageHandler getImageHandler(String imageType){
        for(ImageHandler handler : handlers){
            if(StringUtils.equals(imageType, handler.getImageType())){
                return handler;
            }
        }
        return null;
    }

    public ImageHandler getImageHandler() {
        if(StringUtils.isNotEmpty(imageType)) {
            for(ImageHandler handler : handlers){
                if(StringUtils.equals(imageType, handler.getImageType())){
                    return handler;
                }
            }
        }
        return null;
    }

}
