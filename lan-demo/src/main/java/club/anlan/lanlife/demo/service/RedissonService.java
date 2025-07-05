package club.anlan.lanlife.demo.service;

import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedissonService {

    private final RedissonClient redissonClient;

    public RedissonService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 分布式锁示例 - 模拟秒杀场景
     */
    public String seckill(String productId) {
        // 获取分布式锁，锁的名称可以根据业务场景定义
        RLock lock = redissonClient.getLock("seckill:" + productId);

        try {
            // 尝试获取锁，最多等待10秒，持有锁30秒后自动释放
            boolean isLocked = lock.tryLock(10, 30, TimeUnit.SECONDS);
            if (isLocked) {
                try {
                    // 模拟库存检查和扣减操作
                    RMap<String, Integer> stockMap = redissonClient.getMap("stock");
                    Integer stock = stockMap.get(productId);
                    if (stock != null && stock > 0) {
                        stockMap.put(productId, stock - 1);
                        return "秒杀成功，剩余库存: " + (stock - 1);
                    } else {
                        return "秒杀失败，库存不足";
                    }
                } finally {
                    // 释放锁
                    lock.unlock();
                }
            } else {
                return "获取锁失败，请稍后重试";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "秒杀过程中发生异常";
        }
    }

    /**
     * 分布式集合示例 - 使用分布式Map存储用户信息
     */
    public void saveUserInfo(String userId, String userName) {
        // 获取分布式Map
        RMap<String, String> userMap = redissonClient.getMap("users");
        // 存储用户信息
        userMap.put(userId, userName);
    }

    /**
     * 获取用户信息
     */
    public String getUserInfo(String userId) {
        RMap<String, String> userMap = redissonClient.getMap("users");
        return userMap.get(userId);
    }
}