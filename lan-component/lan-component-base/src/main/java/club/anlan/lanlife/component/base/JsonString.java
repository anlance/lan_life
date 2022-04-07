package club.anlan.lanlife.component.base;

import com.alibaba.fastjson.JSON;

/**
 * json 格式化接口
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/30 0:55
 */
public interface JsonString {
    default String toJsonString() {
        return JSON.toJSONString(this);
    }
}
