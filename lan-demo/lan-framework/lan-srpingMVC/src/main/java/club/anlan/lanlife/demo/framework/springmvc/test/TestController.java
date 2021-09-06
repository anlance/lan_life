package club.anlan.lanlife.demo.framework.springmvc.test;

import club.anlan.lanlife.demo.framework.springmvc.annotation.LanController;
import club.anlan.lanlife.demo.framework.springmvc.annotation.LanRequestMapping;
import club.anlan.lanlife.demo.framework.springmvc.annotation.LanRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/6 23:58
 */
@LanController
@LanRequestMapping("/lan_test")
public class TestController {

    @LanRequestMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response, @LanRequestParam("name") String name) throws IOException {
        System.out.println("test success" + name);
        response.getWriter().write("test success" + name);
    }
}
