package club.anlan.lanlife.basic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/5/1 12:10
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(512*1024*1000);
        multipartResolver.setMaxUploadSizePerFile(512*1024*1000);
        multipartResolver.setMaxInMemorySize(1024*1000);
        return multipartResolver;
    }
}

