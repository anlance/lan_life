package club.anlan.lanlife.auth.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WebMVCConfiguration
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 18:19
 */
@EnableWebMvc
@Configuration
public class WebMVCConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Bean
    public ExceptionHandlerExceptionResolver handlerExceptionResolver() {
        MyExceptionHandlerExceptionResolver exceptionResolver = new MyExceptionHandlerExceptionResolver();
        exceptionResolver.setOrder(0);
        exceptionResolver.setMessageConverters(messageConverters());
        return exceptionResolver;
    }

    private MappingJackson2HttpMessageConverter jsonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_HTML,
                MediaType.TEXT_XML,
                MediaType.APPLICATION_OCTET_STREAM);
        converter.setSupportedMediaTypes(mediaTypes);
        return converter;
    }

    private List<HttpMessageConverter<?>> messageConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(jsonHttpMessageConverter());
        //messageConverters.add(new FastJsonHttpMessageConverter());
        return messageConverters;
    }

}
