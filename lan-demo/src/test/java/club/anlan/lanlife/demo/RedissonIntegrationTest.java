//package club.anlan.lanlife.demo;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.redisson.api.RMap;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class RedissonIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @BeforeEach
//    void setUp() {
//        // 初始化库存数据
//        RMap<String, Integer> stockMap = redissonClient.getMap("stock");
//        stockMap.put("product1", 10);
//
//        // 清空用户数据
//        RMap<String, String> userMap = redissonClient.getMap("users");
//        userMap.clear();
//    }
//
//    @Test
//    void testSeckillSuccess() {
//        // 测试秒杀成功
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                "/api/redisson/seckill/product1", null, String.class);
//
//        assertTrue(response.getBody().contains("秒杀成功"));
//        assertTrue(response.getBody().contains("剩余库存: 9"));
//    }
//
//    @Test
//    void testSaveAndGetUser() {
//        // 测试保存和获取用户信息
//        String userId = "user1";
//        String userName = "张三";
//
//        // 保存用户信息
//        restTemplate.exchange(
//                "/api/redisson/users/{userId}",
//                HttpMethod.POST,
//                new HttpEntity<>(userName),
//                Void.class,
//                userId
//        );
//
//        // 获取用户信息
//        ResponseEntity<String> response = restTemplate.getForEntity(
//                "/api/redisson/users/{userId}", String.class, userId);
//
//        assertEquals(userName, response.getBody());
//    }
//}
