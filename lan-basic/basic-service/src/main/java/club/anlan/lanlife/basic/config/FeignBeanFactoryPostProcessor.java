package club.anlan.lanlife.basic.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * 解决实例停机时，报eurekaAutoServiceRegistration异常问题
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 16:45
 */
@Component
public class FeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if(containsBeanDefinition(beanFactory,"feignContext","eurekaAutoServiceRegistration")) {
            BeanDefinition bd = beanFactory.getBeanDefinition("feignContext");
            bd.setDependsOn("eurekaAutoServiceRegistration");
        }
    }

    private boolean containsBeanDefinition(final ConfigurableListableBeanFactory beanFactory,String... beans) {
        return Arrays.stream(beans).allMatch(new Predicate<String>() {
            @Override
            public boolean test(String b) {
                return beanFactory.containsBeanDefinition(b);
            }
        });
    }

}
