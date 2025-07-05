package club.anlan.lanlife.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        // 创建配置，连接到Redis单节点
        Config config = new Config();
//        // 这里使用默认本地Redis，实际环境请替换为你的Redis地址
        config.useSingleServer().setAddress("redis://192.168.31.162:6379")
                .setPassword("GSSG255211redis");

//         可选：配置连接池大小、超时时间等
//         config.useSingleServer()
//              .setConnectionPoolSize(64)
//              .setConnectionMinimumIdleSize(10)
//              .setTimeout(3000);

        // 集群模式配置示例
        // config.useClusterServers()
        //      .addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001")
        //      .addNodeAddress("redis://127.0.0.1:7002");

        return Redisson.create(config);
    }
}
