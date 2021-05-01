package club.anlan.lanlife.basic;

import club.anlan.lanlife.base.spring.AppContext;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * BasicApplication
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/3 21:04
 */
@SpringBootApplication
@EnableAsync
@ServletComponentScan(basePackages = { "club.anlan.lanlife" })
@ComponentScan(basePackages = "club.anlan.lanlife")
@EnableFeignClients("club.anlan.lanlife")
@MapperScan(basePackages = {"club.anlan.lanlife.basic.mapper"})
public class BasicApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BasicApplication.class);
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(BasicApplication.class, args);
        AppContext.setContext(applicationContext, true);
    }
}
