package club.anlan.lanlife.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * 启动类
 *
 * @author lan
 * @version 1.0
 * @date 2024/8/4 15:14
 */
@SpringBootApplication
@EnableAsync
@ServletComponentScan(basePackages = {"club.anlan.lanlife"})
@ComponentScan(basePackages = "club.anlan.lanlife")
//@EnableFeignClients("club.anlan.lanlife.demo")
//@MapperScan(basePackages = {"club.anlan.lanlife.demo.mapper"})
public class DemoApplication {
    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class);
    }
}
