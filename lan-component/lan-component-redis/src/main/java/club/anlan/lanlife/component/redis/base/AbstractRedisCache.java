package club.anlan.lanlife.component.redis.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 14:27
 */
@Slf4j
public abstract class AbstractRedisCache<K, V> extends BaseRedisCache {

    public abstract K getKey(K key);

    public void add(K key, V value) {
        log.info("#Redis [opsForValue] [add] key: {},value: {}", this.getKey(key), value);
        redisTemplate.opsForValue().set(this.getKey(key), value);
    }

    public void add(K key, V value, long timeout, TimeUnit timeUnit) {
        log.info("#Redis [opsForValue] [add] key: {},value: {}, timeout: {}", key, value, timeout);
        redisTemplate.opsForValue().set(this.getKey(key), value, timeout, timeUnit);
    }

    public boolean delete(K key) {
        log.info("#Redis [opsForValue] [delete] key: {}", this.getKey(key));
        redisTemplate.delete(this.getKey(key));
        return true;
    }

    public void update(K key, V value) {
        log.info("#Redis [opsForValue] [update] key: {},value: {}", this.getKey(key), value);
        if (redisTemplate.opsForValue().get(this.getKey(key)) != null) {
            redisTemplate.opsForValue().set(this.getKey(key), value);
        }
    }

    public void update(K key, long timeout, TimeUnit timeUnit) {
        log.info("#Redis [opsForValue] [update] key: {},timeout: {}", this.getKey(key), timeout);
        V value = (V) redisTemplate.opsForValue().get(this.getKey(key));
        if (value != null) {
            redisTemplate.opsForValue().set(this.getKey(key), value, timeout, timeUnit);
        }
    }

    public void update(K key, V value, long timeout, TimeUnit timeUnit) {
        log.info("#Redis [opsForValue] [update] key: {},value: {}, timeout: {}", this.getKey(key), value, timeout);
        if (redisTemplate.opsForValue().get(this.getKey(key)) != null) {
            redisTemplate.opsForValue().set(this.getKey(key), value, timeout, timeUnit);
        } else {
            log.info("redis value is null.");
        }
    }

    public V get(K key) {
        log.info("#Redis [opsForValue] [get] key: {}", this.getKey(key));
        return (V) redisTemplate.opsForValue().get(this.getKey(key));
    }

    public Long increment(K key) {
        log.info("#Redis [opsForValue] [increment] key: {}", this.getKey(key));
        return redisTemplate.opsForValue().increment(this.getKey(key), 1L);
    }

    public void expire(K key, long timeout, TimeUnit timeUnit) {
        log.info("#Redis [opsForValue] [expire] key: {},timeout: {}", this.getKey(key), timeout);
        redisTemplate.expire(this.getKey(key), timeout, timeUnit);
    }

    public Long ttl(K key) {
        log.info("#Redis [ttl] key:{}", this.getKey(key));
        byte[] keyByte = rawKey(this.getKey(key));
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        if (redisConnection == null) {
            return null;
        }
        Long result = null;
        try {
            result = redisConnection.ttl(keyByte);
        } finally {
            redisConnection.close();
        }
        return result;
    }
}
