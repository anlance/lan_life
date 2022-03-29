package club.anlan.lanlife.component.base.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 应用程序上下文处理类
 *
 * @author lan
 * @version 1.0
 * @date 2021/3/28 13:07
 */
public class AppContext implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(AppContext.class);

    // 上下文对象
    private static ApplicationContext context = null;

    // 标识当前容器服务是否启动完毕
    private static Boolean serviceSatrtCompleted = false;


    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (AppContext.context == null) {
            AppContext.setContext(context, false);
            logger.info("ApplicationContext init successfully");
        }
    }

    /**
     * 从容器中获取已经注入了的 bean 实例对象
     *
     * @param name 获取的 bean 实例名称
     * @return 实例 bean 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        if (context == null) {
            throw new IllegalStateException("applicaitonContext inject failure，please restart the service");
        }
        try {
            return (T) context.getBean(name);
        } catch (BeansException e) {
            logger.error(e.getMessage(), e);
        }
        return (T) null;
    }

    /**
     * 从容器中获取已经注入了的 bean 实例对象
     *
     * @param clazz 获取的 bean 实例 class 类型
     * @return 实例 bean 对象
     */
    public static <T> T getBean(Class<T> clazz) {
        if (context == null) {
            throw new IllegalStateException("applicaitonContext inject failure，please restart the service");
        }
        try {
            return context.getBean(clazz);
        } catch (BeansException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 动态注入一个 bean 到 Spring 容器中
     *
     * @param clazz    bean 实例 class 类型
     * @param beanName 需要注入的 bean 实例名称
     */
    public static <T> void setBean(Class<T> clazz, String beanName) {
        if (!context.containsBean(beanName)) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            //beanDefinitionBuilder.addPropertyValue("servicename", beanName);
            registerBean(beanName, beanDefinitionBuilder.getRawBeanDefinition());
        }
    }

    /**
     * 判断容器是否启动完成
     */
    public static boolean isStartCompleted() {
        return serviceSatrtCompleted;
    }

    /**
     * 获取全局 ApplicationContext 对象
     */
    public static ApplicationContext getContext() {
        if (context == null) {
            throw new IllegalStateException("applicaitonContext inject failure，please restart the service");
        }
        return context;
    }

    /**
     * 设置 ApplicationContext 对象
     */
    public static void setContext(ApplicationContext context, boolean isStartCompleted) {
        if (AppContext.context == null && context != null) {
            AppContext.context = context;
            serviceSatrtCompleted = isStartCompleted;
            logger.info("Set ApplicationContext successfully");
        }
    }

    /**
     * 注册bean到容器
     *
     * @param beanName
     * @param beanDefinition
     */
    private static void registerBean(String beanName, BeanDefinition beanDefinition) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        beanDefinitonRegistry.registerBeanDefinition(beanName, beanDefinition);
    }
}
