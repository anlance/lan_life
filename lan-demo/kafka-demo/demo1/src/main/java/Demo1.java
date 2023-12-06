import club.anlan.lanlife.component.base.spring.AppContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/5 16:58
 */
@SpringBootApplication
@ServletComponentScan(basePackages = {"club.anlan.lanlife"})
@ComponentScan(basePackages = "club.anlan.lanlife")
@EnableFeignClients("club.anlan.lanlife")
public class Demo1 extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Demo1.class);
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Demo1.class, args);
        AppContext.setContext(applicationContext, true);
    }
}
