package club.anlan.lanlife.component.redis.base;

import club.anlan.lanlife.component.utils.DateUtil;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.component.utils.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/5 23:41
 */
@Slf4j
public abstract class AbstractRedisHashCache<K, HK, HV> extends BaseRedisCache<K, HashMap<HK, HV>> {


    /**
     * @Fields MAXINUM_PER_TIME: 每次从数据库查询的最大数量
     */
    protected static final int MAXINUM_PER_TIME = 10000;

    /**
     * 获取 key
     *
     * @return
     */
    public abstract K getKey();

    public void add(HK hashKey, HV value) {
        log.info("#Redis [HashOperations] [add] key: {},hashKey: {},value: {}", this.getKey(), hashKey, value);
        redisTemplate.opsForHash().put(this.getKey(), hashKey, value);
    }

    public void add(Map<HK, HV> map) {
        log.debug("#Redis [HashOperations] [addMap] key: {}", this.getKey());
        if (map == null || map.isEmpty()) {
            return;
        }
        redisTemplate.opsForHash().putAll(this.getKey(), map);
    }

    @SuppressWarnings("unchecked")
    public boolean delete(HK... hashKeys) {
        log.info("#Redis [HashOperations] [delete] key: {},hashKeys: {}", this.getKey(),
                JSON.toJSONString(hashKeys));
        if (ArrayUtils.isEmpty(hashKeys)) {
            return false;
        }
        redisTemplate.opsForHash().delete(this.getKey(), hashKeys);
        return true;
    }

    public boolean deleteAll() {
        log.info("#Redis [HashOperations] [deleteAll] key: {}", this.getKey());
        redisTemplate.delete(this.getKey());
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean deleteByValue(HV hashValue) {
        log.info("#Redis [HashOperations] [deleteByValue] key: {},value: {}", this.getKey(),
                JSON.toJSONString(hashValue));
        Map<HK, HV> resultMap = this.getAll();
        if (resultMap == null || resultMap.isEmpty()) {
            return false;
        }
        for (Map.Entry<HK, HV> entry : resultMap.entrySet()) {
            if (entry.getValue().equals(hashValue)) {
                this.delete(entry.getKey());
                return true;
            }
        }
        return false;
    }

    public void update(HK hashKey, HV value) {
        log.info("#Redis [HashOperations] [update] key: {},hashKey: {},value: {}", this.getKey(), hashKey, value);
        if (StringUtil.isEmpty((CharSequence) this.getKey()) || StringUtil.isEmpty((CharSequence) hashKey)) {
            return;
        }
        HashOperations<K, HK, HV> hashOperations = redisTemplate.opsForHash();
        if (hashOperations.get(this.getKey(), hashKey) != null) {
            hashOperations.put(this.getKey(), hashKey, value);
        }
    }

    @SuppressWarnings("unchecked")
    public HV get(HK hashKey) {
        log.debug("#Redis [HashOperations] [get] key: {},hashKey: {}", this.getKey(), hashKey);
        if (hashKey != null) {
            Object obj = redisTemplate.opsForHash().get(this.getKey(), hashKey);
            if (obj != null) {
                return (HV) obj;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<HV> multiGet(Collection<HK> hashKeys) {
        log.debug("#Redis [HashOperations] [get] key: {},hashKeys: {}", this.getKey(), JSON.toJSONString(hashKeys));
        if (!CollectionUtil.isEmpty(hashKeys)) {
            List<Object> objs = redisTemplate.opsForHash().multiGet(this.getKey(), (Collection<Object>) hashKeys);
            if (objs != null) {
                return (List<HV>) objs;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<HK, HV> getAll() {
        log.debug("#Redis [HashOperations] [getAll] key: {}", this.getKey());
        Map<HK, HV> resultMap = null;
        if (!StringUtil.isEmpty((CharSequence) this.getKey())) {
            Map<Object, Object> objectMap = redisTemplate.opsForHash().entries(this.getKey());
            resultMap = new LinkedHashMap<HK, HV>();
            for (Map.Entry<Object, Object> entry : objectMap.entrySet()) {
                resultMap.put((HK) entry.getKey(), (HV) entry.getValue());
            }
        }
        return resultMap;
    }

    public Map<HK, HV> fetchAll() {
        log.debug("#Redis [HashOperations] [fetchAll] key: {}", this.getKey());
        final K key = this.getKey();
        Map<byte[], byte[]> entries = redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {

            @Override
            @SuppressWarnings("unchecked")
            public Map<byte[], byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = rawKey(key);
                connection.multi();
                connection.hGetAll(keyByte);
                connection.del(keyByte);
                List<Object> resultsList = connection.exec();
                if (!CollectionUtil.isEmpty(resultsList)) {
                    Map<byte[], byte[]> valueMap = (Map<byte[], byte[]>) resultsList.get(0);
                    return valueMap;
                }
                return null;
            }
        }, true);

        return deserializeHashMap(entries);
    }

    public HK getByValue(HV hashValue) {
        log.debug("#Redis [HashOperations] [getByValue] key: {},value: {}", this.getKey(), hashValue);
        Map<HK, HV> resultMap = this.getAll();
        if (resultMap == null || resultMap.isEmpty()) {
            return null;
        }
        Set<Map.Entry<HK, HV>> entries = resultMap.entrySet();
        Iterator<Map.Entry<HK, HV>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<HK, HV> entry = iterator.next();
            if (hashValue.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean hasKey(HK hashKey) {
        return redisTemplate.opsForHash().hasKey(this.getKey(), hashKey);
    }

    public void expireAt(Date date) {
        log.debug("#Redis [expireAt] key: {},date: {}", this.getKey(),
                DateUtil.dateToString(date, DateUtil.DATA_FORMAT_YYYY_MM_DD_HH_MM_SS));
        redisTemplate.expireAt(this.getKey(), date);
    }

    public void expire(long timeout, TimeUnit timeUnit) {
        log.debug("#Redis [expire] key: {},timeout: {},TimeUnit: {}", this.getKey(), timeout, timeUnit.name());
        redisTemplate.expire(this.getKey(), timeout, timeUnit);
    }

    public void flushAll() {
        log.info("#Redis [flushAll]");
        redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            connection.flushAll();
            return true;
        });
    }

}

