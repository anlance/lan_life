package club.anlan.lanlife.basic.redis.cache;

import club.anlan.lanlife.base.util.JsonUtil;
import club.anlan.lanlife.basic.redis.RedisKey;
import club.anlan.lanlife.redis.base.RedisCache;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;

/**
 * ContentTypeCache
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 14:38
 */
@Component
@Slf4j
public class ContentTypeCache extends RedisCache<String, String> implements CommandLineRunner {

    /**
     * @Fields timeout: 过期时间（秒）
     */
    public final int TIMEOUT = 1800;

    @Override
    public String getKey(String key) {
        return String.format(RedisKey.CONTENT_TYPE_CACHE, key);
    }

    public void init() {
        log.info("init ContentTypeCache start ----------------------------------->");
        InputStream is = this.getClass().getResourceAsStream("/contentType.json");
        String str = JsonUtil.readJsonFile(is);
        process(str);
        log.info("init ContentTypeCache end ----------------------------------->");
    }


    private void process(String txtStr) {
        JSONObject json = JSONObject.parseObject(txtStr);
        JSONArray dataList = json.getJSONArray("data");
        for (int i = 0; i < dataList.size(); i++) {
            JSONObject obj = dataList.getJSONObject(i);
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                super.add(entry.getKey(), String.valueOf(entry.getValue()));
            }

        }

    }

    @Override
    public void run(String... args) throws Exception {
        init();
    }
}
