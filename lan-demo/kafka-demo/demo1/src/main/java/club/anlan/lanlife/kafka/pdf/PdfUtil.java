package club.anlan.lanlife.kafka.pdf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/18 22:51
 */
@Slf4j
@Component
public class PdfUtil {

    @Resource
    private HtmlUtil htmlUtil;

    @PostConstruct
    public void test() {
        log.info("start");
        Map<String, Object> map = new HashMap<>();
        List<Order.Detail> list = new ArrayList<>();
        list.add(Order.Detail.valueOf(1,new Date(), "100", "100"));
        list.add(Order.Detail.valueOf(2,new Date(), "200", "200"));
        list.add(Order.Detail.valueOf(3,new Date(), "300", "300"));
        list.add(Order.Detail.valueOf(4,new Date(), "400", "300"));
        list.add(Order.Detail.valueOf(5,new Date(), "500", "300"));
        List<Order> orderList = new ArrayList<>();
        Order order = Order.valueOf("测试充电站", list);
        orderList.add(order);
        orderList.add(order);
        orderList.add(order);
        orderList.add(order);
        orderList.add(order);
        orderList.add(order);
        orderList.add(order);
        orderList.add(order);
        map.put("orderList", orderList);
        String test = htmlUtil.createHtml("test", "1", map);
        if (test != null) {
            System.out.println(test);
        }
        log.info("end: {}", test);
    }
}
