package club.anlan.lanlife.demo.transaction;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 事务基本测试类
 *
 * @author lan
 * @version 1.0
 * @date 2021/8/26 0:18
 */
@Slf4j
@SpringBootTest
@DisplayName("test transaction simple")
public class TransSimple {

    @Test
    public void testBack() {
        Assertions.assertTrue(true);
    }
}
