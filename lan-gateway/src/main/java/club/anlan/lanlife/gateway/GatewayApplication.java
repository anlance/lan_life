package club.anlan.lanlife.gateway;

import club.anlan.lanlife.base.spring.AppContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 网关启动
 *
 * @author lan
 * @version 1.0
 * @date 2021/3/28 12:55
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@ComponentScan(basePackages = "club.anlan.lanlife")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ServletComponentScan(basePackages = { "club.anlan.lanlife" })
@Slf4j
public class GatewayApplication {

    @Value("${cors.allowed.origin}")
    private String origins;

    @Value("${cors.allowed.header}")
    private String header;

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(GatewayApplication.class, args);
        AppContext.setContext(applicationContext, true);
    }

    /**
     *
     * attention:简单跨域就是GET，HEAD和POST请求，但是POST请求的"Content-Type"只能是application/x-www-form-urlencoded, multipart/form-data 或 text/plain
     * 反之，就是非简单跨域，此跨域有一个预检机制，说直白点，就是会发两次请求，一次OPTIONS请求，一次真正的请求
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(true);
        log.info("--------------------------- {}", origins);
        if(StringUtils.isNotEmpty(origins)) {
            String[] originArray = origins.split(",");
            for(String origin : originArray) {
                if(StringUtils.isNotEmpty(origin)) {
                    // #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
                    config.addAllowedOrigin(origin);
                }
            }
        }
        // #允许访问的头信息,*表示全部
        config.addAllowedHeader(header);
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        config.setAllowedOrigins(Arrays.asList(origins.split(",")));
        // 允许提交请求的方法，*表示全部允许
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        // 允许Get的请求方法
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
