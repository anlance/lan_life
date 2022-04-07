package club.anlan.lanlife.component.redis.util;

import club.anlan.lanlife.component.base.util.DateUtil;
import club.anlan.lanlife.component.redis.base.AbstractRedisCache;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 14:29
 */
@Component
@Slf4j
public class RedisUtil extends AbstractRedisCache<String, String> {


    @Override
    public String getKey(String key) {
        return null;
    }

    /**
     * 新增String缓存
     */
    @Override
    public void add(String key, String value) {
        log.info("#Redis [ValueOperations] [set] key:{} ,value: {}", key, value);
        redisTemplate.opsForValue().set(key, value.toString());
    }

    /**
     * 新增String缓存，并设置有效期（单位：秒）
     */
    public void add(String key, String value, long seconds) {
        this.add(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 新增String缓存，并设置有效期
     */
    @Override
    public void add(final String key, final String value, long timeout, TimeUnit timeUnit) {
        log.info("#Redis [ValueOperations] [set] key: " + key + ",value: " + value + ",timeout: " + timeout
                + ",timeUnit: " + timeUnit.name());
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }


    /**
     * String格式追加
     */
    public void append(String key, String value) {
        log.info("#Redis [ValueOperations] [append] key: {} ,value: {}", key, value);
        redisTemplate.opsForValue().append(key, value);
    }

    /**
     * 更新，如果不存在返回false
     */
    @Override
    public void update(String key, String value) {
        log.info("#Redis [ValueOperations] [update] key: {} ,value: {}", key, value);
        if (redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 获取String value
     */
    @Override
    public String get(String key) {
        log.info("#Redis [ValueOperations] [get] key: {}", key);
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 单条删除
     */
    @Override
    public boolean delete(String key) {
        log.info("#Redis [delete] key: {}", key);
        redisTemplate.delete(key);
        return true;
    }

    /**
     * 批量删除
     */
    public void delete(List<String> keys) {
        log.info("#Redis [delete] key: {}", JSON.toJSONString(keys));
        redisTemplate.delete(keys);
    }

    /**
     * 新增Hash格式数据
     */
    public void addMap(final String key, final String hashKey, final String value) {
        log.info("#Redis [HashOperations] [put] key: {},hashKey: {},value: {}", key, hashKey, value);
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey)) {
            return;
        }
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @SuppressWarnings("unchecked")
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = getKeySerializer().serialize(key);
                byte[] hashKeyByte = getHashKeySerializer().serialize(hashKey);
                byte[] hashValueByte = getHashValueSerializer().serialize(value);
                return connection.hSet(keyByte, hashKeyByte, hashValueByte);
            }
        });
    }


    /**
     * 删除Hash结构数据
     */
    public void deleteMap(final String key, final Object... hashKeys) {
        log.info("#Redis [HashOperations] [delete] key: {} ,hashKeys: {}", key, JSON.toJSONString(hashKeys));
        if (StringUtils.isEmpty(key) || hashKeys == null) {
            return;
        }
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            byte[] keyByte = getKeySerializer().serialize(key);
            List<byte[]> hashKeyList = new ArrayList<>(hashKeys.length);
            for (Object obj : hashKeys) {
                hashKeyList.add(getHashKeySerializer().serialize(obj));
            }
            return connection.hDel(keyByte, hashKeyList.toArray(new byte[][]{}));
        });
    }


    /**
     * 获取单个Hash结构数据
     */
    public String getMapOne(final String key, final String hashKey) {
        log.info("#Redis [HashOperations] [get] key: {},hashKey: {}", key, hashKey);
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey)) {
            return null;
        }
        return (String) redisTemplate.execute(new RedisCallback<String>() {
            @SuppressWarnings("unchecked")
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = getKeySerializer().serialize(key);
                byte[] hashKeyByte = getHashKeySerializer().serialize(hashKey);
                byte[] hashValueByte = connection.hGet(keyByte, hashKeyByte);
                if (hashValueByte != null) {
                    return getHashValueSerializer().deserialize(hashValueByte).toString();
                }
                return null;
            }
        });
    }

    /**
     * 新增List结构数据
     */
    public void addList(String key, String value) {
        log.info("#Redis [ListOperations] [leftPushAll] key: {} ,value: {}", key, value);
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 新增List结构数据
     */
    public void addListAll(String key, String... array) {
        log.info("#Redis [ListOperations] [leftPushAll] key: {} ,array: {}", key, JSON.toJSONString(array));
        redisTemplate.opsForList().rightPushAll(key, array);
    }

    /**
     * 获取List结构数据
     */
    public List<String> getList(String key) {
        return this.getList(key, 0, -1);
    }

    /**
     * 获取List结构数据数量
     */
    public Long getListSize(String key) {
        if (this.hasKey(key)) {
            return redisTemplate.opsForList().size(key);
        }
        return 0L;
    }

    /**
     * 获取List结构数据
     */
    public List<String> getList(String key, long arg1, long arg2) {
        log.info("#Redis [ListOperations] [range] key: " + key + ",arg1: " + arg1 + ",arg2: " + arg2);
        if (this.hasKey(key)) {
            return redisTemplate.opsForList().range(key, arg1, arg2);
        }
        return null;
    }

    /**
     * 设置缓存超时时间
     */
    public void expireAt(String key, Date date) {
        log.info("#Redis [expireAt] key: {} ,date: {}", key,
                DateUtil.dateToString(date, DateUtil.DATA_FORMAT_yyyy_MM_dd_HH_mm_ss));
        redisTemplate.expireAt(key, date);
    }

    /**
     * 设置缓存有效期，单位：秒
     */
    public void expire(String key, long timeout) {
        log.info("#Redis [expire] key: {}, timeout: {}", key, timeout);
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存有效期
     */
    @Override
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        log.info("#Redis [expire] key: {}, timeout: {},TimeUnit: {}", key, timeout, timeUnit.name());
        redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获取分布式锁
     */
    public boolean lock(final String key, final String value, final long seconds) {
        log.info("#Redis [lock] key: {},value: {},seconds: {}", key, value, seconds);
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        return (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = rawKey(key);
                byte[] valueByte = rawValue(value);
                Boolean result = connection.setNX(keyByte, valueByte);
                if (result != null && result.booleanValue()) {
                    result = connection.expire(keyByte, seconds);
                    if (result != null && result.booleanValue()) {
                        return true;
                    } else {
                        connection.del(keyByte);
                    }
                }
                return false;
            }
        });
    }

    /**
     * 释放锁
     */
    public void unlock(String key) {
        log.info("#Redis [delete] key: {}", key);
        redisTemplate.delete(key);
    }

    /**
     * 判断是否存在缓存
     */
    public boolean hasKey(String key) {
        log.info("#Redis [hasKey] key: {}", key);
        return redisTemplate.hasKey(key);
    }

    /**
     * 清空redis缓存
     */
    public void flushAll() {
        log.info("#Redis [flushAll]");
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushAll();
                return true;
            }
        });
    }

    /**
     * 获取递增值
     */
    public Long incr(String key) {
        log.info("#Redis [incr] key: {}", key);
        return redisTemplate.getConnectionFactory().getConnection().incr(rawKey(key));
    }

    /**
     * 获取递减值
     */
    public Long decr(String key) {
        log.info("#Redis [incr] key: {}", key);
        return redisTemplate.getConnectionFactory().getConnection().decr(rawKey(key));
    }

    /**
     * 分布式加锁
     */
    public boolean lock(final String key, final String value, Long timeout, TimeUnit timeUnit) {
        log.info("#Redis [lock] key:{}, value: {}, timeout:{} ,TimeUnit:{}", key, value, timeout, timeUnit);
        boolean result = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = rawKey(key);
                byte[] valueByte = rawValue(value);
                return connection.setNX(keyByte, valueByte);
            }
        });
        if (result && timeout != null && timeUnit != null) {
            redisTemplate.expire(key, timeout, timeUnit);
        }
        return result;
    }

    /**
     * 分布式加锁
     */
    public boolean lock(final String key, final String value) {
        return lock(key, value, null, null);
    }

    public void setRange(final String key, final Object value, int offset) {
        log.info("#Redis [setRange] key:{}, value: {}, offset:{}", key, value, offset);
        redisTemplate.opsForValue().set(key, value.toString(), offset);
    }
}
