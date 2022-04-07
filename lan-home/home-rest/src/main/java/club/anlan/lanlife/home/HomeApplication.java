package club.anlan.lanlife.home;

import club.anlan.lanlife.component.base.spring.AppContext;
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
 * home 启动类
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/11 17:25
 */
@SpringBootApplication
@EnableAsync
@ServletComponentScan(basePackages = {"club.anlan.lanlife"})
@ComponentScan(basePackages = "club.anlan.lanlife")
@EnableFeignClients("club.anlan.lanlife")
@MapperScan(basePackages = {"club.anlan.lanlife.home.mapper"})
public class HomeApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(HomeApplication.class);
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(HomeApplication.class, args);
        AppContext.setContext(applicationContext, true);
    }
}
