package club.anlan.lanlife.component.redis.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 14:27
 */
public abstract class AbstractRedisCache<K, V> {

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

    @SuppressWarnings({"unchecked", "rawtypes"})
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
}
