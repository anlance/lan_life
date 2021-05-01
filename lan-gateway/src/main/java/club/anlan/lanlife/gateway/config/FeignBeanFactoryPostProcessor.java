//package club.anlan.lanlife.gateway.config;
//
//import java.util.Arrays;
//import java.util.function.Predicate;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.stereotype.Component;
//
///**
// * 类
// *
// * @author lan
// * @version 1.0
// * @date 2021/4/30 0:44
// */
//@Component
//public class FeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor{
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        if(containsBeanDefinition(beanFactory,"feignContext","eurekaAutoServiceRegistration")) {
//            BeanDefinition bd = beanFactory.getBeanDefinition("feignContext");
//            bd.setDependsOn("eurekaAutoServiceRegistration","registrationListener");
//        }
//    }
//
//    private boolean containsBeanDefinition(final ConfigurableListableBeanFactory beanFactory,String... beans) {
//        return Arrays.stream(beans).allMatch(new Predicate<String>() {
//            @Override
//            public boolean test(String b) {
//                return beanFactory.containsBeanDefinition(b);
//            }
//        });
//    }
//
//}