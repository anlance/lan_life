package club.anlan.lanlife.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 类
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
//        ScheduledThreadPoolExecutor ex = new ScheduledThreadPoolExecutor(1);
//        ex.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
//        ex.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
//        ex.setRemoveOnCancelPolicy(true);
//
//        ex.submit(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("start");
//                for (int i=0;i<5;i++){
//                    System.out.println("sl :"+i);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e){
//
//                    }
//                }
//                System.out.println("end");
//            }
//        });
//        ex.shutdown();
        SpringApplication.run(DemoApplication.class);
    }
}
