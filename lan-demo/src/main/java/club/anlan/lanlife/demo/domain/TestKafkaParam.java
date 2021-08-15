package club.anlan.lanlife.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 测试 kafka参数
 *
 * @author lan
 * @version 1.0
 * @date 2021/8/15 22:13
 */
@Data
@AllArgsConstructor
public class TestKafkaParam {

    private Integer value;

    private Date time;
}
