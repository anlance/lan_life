package club.anlan.lanlife.cache;

import club.anlan.lanlife.util.JsonUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ContentTypeCache
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/5 17:15
 */
@Component
@Slf4j
public class ContentTypeCache implements BeanPostProcessor {

    private Map<String, String> map = new HashMap<>();

    public void init() {
        log.info("init start ----------------------------------->");
        InputStream is = this.getClass().getResourceAsStream("/contentType.json");
//        String url = ContentTypeCache.class.getResource("/contentType.json").getFile();
        String str = JsonUtil.readJsonFile(is);
        process(str);
    }


    private void process(String txtStr) {
        JSONObject json = JSONObject.parseObject(txtStr);
        JSONArray dataList = json.getJSONArray("data");
        for (int i = 0; i < dataList.size(); i++) {
            JSONObject obj = dataList.getJSONObject(i);
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                map.put(entry.getKey(), String.valueOf(entry.getValue()));
            }

        }

    }

    public Map<String, String> getMap() {
        return map;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 初始化缓存
        init();
        return bean;
    }
}
