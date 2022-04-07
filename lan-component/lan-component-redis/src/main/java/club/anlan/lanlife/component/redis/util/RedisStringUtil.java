package club.anlan.lanlife.component.redis.util;

import club.anlan.lanlife.component.redis.base.BaseRedisCache;
import club.anlan.lanlife.component.utils.DateUtil;
import club.anlan.lanlife.component.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis String序列化格式存储工具类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/4 19:08
 */
@Slf4j
@Component
public class RedisStringUtil extends BaseRedisCache<String, String> {

    /**
     * 新增String缓存
     */
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
    public boolean update(String key, String value) {
        log.info("#Redis [ValueOperations] [update] key: {} ,value: {}", key, value);
        if (redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, value);
            return true;
        }
        return false;
    }

    /**
     * 获取String value
     */
    public String get(String key) {
        log.info("#Redis [ValueOperations] [get] key: {}", key);
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 单条删除
     */
    public void delete(String key) {
        log.info("#Redis [delete] key: {}", key);
        redisTemplate.delete(key);
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
        if (StringUtil.isEmpty(key) || StringUtil.isEmpty(hashKey)) {
            return;
        }
        // redisTemplate.opsForHash().put(key, hashKey, value);
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
     * 新增Hash结构数据
     */
    public void addMap(final String key, final Map<String, String> map) {
        log.info("#Redis [HashOperations] [putAll] key: {} ,map.size: {}", key, map.size());
        if (StringUtil.isEmpty(key) || map.isEmpty()) {
            return;
        }
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @SuppressWarnings("unchecked")
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = getKeySerializer().serialize(key);
                Map<byte[], byte[]> mapByte = new HashMap<>();
                for (Map.Entry<String, String> m : map.entrySet()) {
                    mapByte.put(getHashKeySerializer().serialize(m.getKey()),
                            getHashValueSerializer().serialize(m.getValue()));
                }
                connection.hMSet(keyByte, mapByte);
                return true;
            }
        });
        // redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 删除Hash结构数据
     */
    public void deleteMap(final String key, final Object... hashKeys) {
        log.info("#Redis [HashOperations] [delete] key: {} ,hashKeys: {}", key, JSON.toJSONString(hashKeys));
        if (StringUtil.isEmpty(key) || hashKeys == null) {
            return;
        }
        // redisTemplate.opsForHash().delete(key, hashKeys);
        redisTemplate.execute(new RedisCallback<Long>() {
            @SuppressWarnings("unchecked")
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = getKeySerializer().serialize(key);
                List<byte[]> hashKeyList = new ArrayList<>(hashKeys.length);
                for (Object obj : hashKeys) {
                    hashKeyList.add(getHashKeySerializer().serialize(obj));
                }
                return connection.hDel(keyByte, hashKeyList.toArray(new byte[][]{}));
            }
        });
    }

    /**
     * 获取Hash结构数据
     */
    public Map<Object, Object> getMap(final String key) {
        log.info("#Redis [HashOperations] [get] key: {}", key);
        // return redisTemplate.opsForHash().entries(key);
        if (StringUtil.isEmpty(key)) {
            return null;
        }
        return redisTemplate.execute(new RedisCallback<Map<Object, Object>>() {
            @SuppressWarnings("unchecked")
            @Override
            public Map<Object, Object> doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = getKeySerializer().serialize(key);
                Map<byte[], byte[]> byteMap = connection.hGetAll(keyByte);
                if (byteMap == null || byteMap.isEmpty()) {
                    return null;
                }
                Map<Object, Object> stringMap = new HashMap<>();
                for (Map.Entry<byte[], byte[]> entry : byteMap.entrySet()) {
                    stringMap.put(getHashKeySerializer().deserialize(entry.getKey()),
                            getHashValueSerializer().deserialize(entry.getValue()));
                }
                return stringMap;
            }
        });
    }

    /**
     * 获取单个Hash结构数据
     */
    public String getMapOne(final String key, final String hashKey) {
        log.info("#Redis [HashOperations] [get] key: {},hashKey: {}", key, hashKey);

        if (StringUtil.isEmpty(key) || StringUtil.isEmpty(hashKey)) {
            return null;
        }
        return redisTemplate.execute(new RedisCallback<String>() {
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
                DateUtil.dateToString(date, DateUtil.DATA_FORMAT_YYYY_MM_DD_HH_MM_SS));
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
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        log.info("#Redis [expire] key: {}, timeout: {},TimeUnit: {}", key, timeout, timeUnit.name());
        redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获取分布式锁
     */
    public boolean lock(final String key, final String value, final long seconds) {
        log.info("#Redis [lock] key: {},value: {},seconds: {}", key, value, seconds);
        if (StringUtil.isEmpty(key)) {
            return false;
        }
        return redisTemplate.execute(new RedisCallback<Boolean>() {
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
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        if (redisConnection == null) {
            return null;
        }
        Long result = null;
        try {
            result = redisConnection.incr(rawKey(key));
        } finally {
            redisConnection.close();
        }
        return result;
    }

    /**
     * 获取递减值
     */
    public Long decr(String key) {
        log.info("#Redis [incr] key: {}", key);
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        if (redisConnection == null) {
            return null;
        }
        Long result = null;
        try {
            result = redisConnection.decr(rawKey(key));
        } finally {
            redisConnection.close();
        }
        return result;
    }

    /**
     * 分布式加锁
     */
    public boolean lock(final String key, final String value, Long timeout, TimeUnit timeUnit) {
        log.info("#Redis [lock] key:{}, value: {}, timeout:{} ,TimeUnit:{}", key, value, timeout, timeUnit);
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
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

    public void setRange(final String key, final Object value, int offset) {
        log.info("#Redis [setRange] key:{}, value: {}, offset:{}", key, value, offset);
        redisTemplate.opsForValue().set(key, value.toString(), offset);
    }

    public Long ttl(String key) {
        log.info("#Redis [ttl] key:{}", key);
        byte[] keyByte = rawKey(key);
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

