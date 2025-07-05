package club.anlan.lanlife.demo.controller;

import club.anlan.lanlife.demo.service.RedissonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/redisson")
public class RedissonController {

    private final RedissonService redissonService;

    public RedissonController(RedissonService redissonService) {
        this.redissonService = redissonService;
    }

    /**
     * 测试分布式锁 - 模拟秒杀
     */
    @PostMapping("/seckill/{productId}")
    public String seckill(@PathVariable String productId) {
        return redissonService.seckill(productId);
    }

    /**
     * 测试分布式Map - 保存用户信息
     */
    @PostMapping("/users/{userId}")
    public void saveUser(@PathVariable String userId, @RequestBody String userName) {
        redissonService.saveUserInfo(userId, userName);
    }

    /**
     * 测试分布式Map - 获取用户信息
     */
    @GetMapping("/users/{userId}")
    public String getUser(@PathVariable String userId) {
        return redissonService.getUserInfo(userId);
    }
}
