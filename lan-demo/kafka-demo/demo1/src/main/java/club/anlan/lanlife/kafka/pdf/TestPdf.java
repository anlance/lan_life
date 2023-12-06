package club.anlan.lanlife.kafka.pdf;

import com.netflix.discovery.shared.Application;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/18 22:35
 */
@SpringBootTest(classes = Application.class)
public class TestPdf {

    @Autowired
    private HtmlUtil htmlUtil;

    @Test
    public void test() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "faker");
        map.put("age", "18");
        map.put("email", "ppopopopo");
        String test = htmlUtil.createHtml("test", "1", map);
        if (test != null) {
            System.out.println(test);
        }
    }
}
