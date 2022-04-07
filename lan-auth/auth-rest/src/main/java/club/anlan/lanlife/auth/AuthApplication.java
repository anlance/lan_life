package club.anlan.lanlife.auth;

import club.anlan.lanlife.component.base.spring.AppContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * auth 启动类
 *
 * @author lan
 * @version 1.0
 * @date 2021/10/7 16:02
 */
@SpringBootApplication
@ComponentScan({ "club.anlan.lanlife" })
@EnableFeignClients("club.anlan.lanlife.auth")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ServletComponentScan(basePackages = { "club.anlan.lanlife.auth" })
public class AuthApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AuthApplication.class, args);
        AppContext.setContext(applicationContext, true);
    }
}
