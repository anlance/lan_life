package club.anlan.lanlife.proxy.server;

import club.anlan.lanlife.component.base.spring.AppContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * proxy server 启动类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 14:47
 */
@Slf4j
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
@ComponentScan(basePackages = "club.anlan.lanlife")
@ServletComponentScan(basePackages = {"club.anlan.lanlife"})
public class ServerApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ServerApplication.class, args);
        AppContext.setContext(applicationContext, true);

        ProxyServerStarter serverStarter = AppContext.getBean(ProxyServerStarter.class);
        serverStarter.start();
    }


}
