package club.anlan.lanlife.local;

import club.anlan.lanlife.base.spring.AppContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 本地服务启动类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/3 21:04
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan(basePackages = { "club.anlan.lanlife" })
@ComponentScan(basePackages = "club.anlan.lanlife")
public class LocalApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(LocalApplication.class);
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(LocalApplication.class, args);
        AppContext.setContext(applicationContext, true);
    }
}
