package club.anlan.lanlife.component.redis.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * redis 缓存抽象
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 19:09
 */
public abstract class BaseRedisCache<K, V> {

    @Autowired
    protected RedisTemplate<K, V> redisTemplate;

    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected RedisSerializer<String> getStringSerializer() {
        return redisTemplate.getStringSerializer();
    }

    @SuppressWarnings("rawtypes")
    protected RedisSerializer getKeySerializer() {
        return redisTemplate.getKeySerializer();
    }

    @SuppressWarnings("rawtypes")
    protected RedisSerializer getValueSerializer() {
        return redisTemplate.getValueSerializer();
    }

    @SuppressWarnings("rawtypes")
    protected RedisSerializer getHashKeySerializer() {
        return redisTemplate.getHashKeySerializer();
    }

    @SuppressWarnings("rawtypes")
    protected RedisSerializer getHashValueSerializer() {
        return new JdkSerializationRedisSerializer();
    }

    @SuppressWarnings("unchecked")
    protected byte[] rawKey(Object key) {
        if ((getKeySerializer() == null) && ((key instanceof byte[]))) {
            return (byte[]) key;
        }
        return getKeySerializer().serialize(key);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected byte[] rawValue(Object value) {
        RedisSerializer redisSerializer = null;
        if (value instanceof String) {
            redisSerializer = getStringSerializer();
        } else {
            redisSerializer = getValueSerializer();
        }
        return redisSerializer.serialize(value);
    }

    public void watch(K hashKey) {
        redisTemplate.watch(hashKey);
    }

    public void multi() {
        redisTemplate.multi();
    }

    public List<Object> exec() {
        return redisTemplate.exec();
    }

    @SuppressWarnings("unchecked")
    protected <HK, HV> Map<HK, HV> deserializeHashMap(Map<byte[], byte[]> entries) {
        // connection in pipeline/multi mode
        if (entries == null) {
            return null;
        }

        Map<HK, HV> map = new LinkedHashMap<HK, HV>(entries.size());

        for (Map.Entry<byte[], byte[]> entry : entries.entrySet()) {
            map.put((HK) deserializeHashKey(entry.getKey()), (HV) deserializeHashValue(entry.getValue()));
        }

        return map;
    }

    @SuppressWarnings({ "unchecked" })
    protected <HK> HK deserializeHashKey(byte[] value) {
        if (redisTemplate.getHashKeySerializer() == null) {
            return (HK) value;
        }
        return (HK) redisTemplate.getHashKeySerializer().deserialize(value);
    }

    @SuppressWarnings("unchecked")
    protected <HV> HV deserializeHashValue(byte[] value) {
        if (redisTemplate.getHashValueSerializer() == null) {
            return (HV) value;
        }
        return (HV) redisTemplate.getHashValueSerializer().deserialize(value);
    }
}

