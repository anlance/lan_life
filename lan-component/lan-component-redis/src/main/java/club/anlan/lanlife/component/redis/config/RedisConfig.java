package club.anlan.lanlife.component.redis.config;

import club.anlan.lanlife.component.base.exception.BusinessException;
import club.anlan.lanlife.component.base.exception.BusinessExceptionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

/**
 * redis config 类
 *
 * @author lan
 * @version 1.0
 * @date 2021/4/24 10:40
 */
@Configuration
@EnableCaching
@Slf4j
public class RedisConfig extends CachingConfigurerSupport implements EnvironmentAware {

    private final String KEY_PREFIX = "spring.redis.";

    private Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    public String getProperty(String key) {
        String completeKey = KEY_PREFIX + key;
        return env.getProperty(completeKey);
    }

    public int getPropertyInt(String key) throws BusinessException {
        String completeKey = KEY_PREFIX + key;
        try {
            return Integer.parseInt(env.getProperty(completeKey));
        } catch (Exception e) {
            throw BusinessExceptionFactory.createBisException("configuration of '" + completeKey + "' is not Integer value");
        }
    }

    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };

    }

    @Primary
    @Bean(name = "cacheManager")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        //设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，JdkSerializationRedisSerializer序列化value,所以以下注释代码为默认实现
        //ClassLoader loader = this.getClass().getClassLoader();
        //JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        //RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
        //RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //设置默认超过期时间是30秒
        defaultCacheConfig.entryTtl(Duration.ofSeconds(30));
        //初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
        //RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory);
        return cacheManager;
    }


    @Bean("redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() throws BusinessException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(getPropertyInt("pool.max-idle"));
        poolConfig.setMaxTotal(getPropertyInt("pool.max-total"));
        poolConfig.setMaxWaitMillis(getPropertyInt("pool.max-wait"));
        poolConfig.setTestOnBorrow(true);

        JedisClientConfiguration.DefaultJedisClientConfigurationBuilder builder = (JedisClientConfiguration.DefaultJedisClientConfigurationBuilder) JedisClientConfiguration
                .builder();
        builder.usePooling();
        builder.poolConfig(poolConfig);
        builder.connectTimeout(Duration.ofMillis(Integer.parseInt(getProperty("connect-timeout"))));
        builder.readTimeout(Duration.ofMillis(Integer.parseInt(getProperty("read-timeout"))));

        String nodes = getProperty("nodes");
        log.info("nodes: {}", nodes);
        JedisConnectionFactory redisConnectionFactory = null;
        if (StringUtils.isEmpty(nodes)) {
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
            configuration.setHostName(getProperty("host"));
            configuration.setPassword(RedisPassword.of(getProperty("password")));
            configuration.setPort(Integer.parseInt(getProperty("port")));
            redisConnectionFactory = new JedisConnectionFactory(configuration, builder.build());
        } else {
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(
                    Arrays.asList(nodes.split(",")));
            redisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, builder.build());
        }
        return redisConnectionFactory;
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws BusinessException {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        template.setStringSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisMessageListenerContainer getRedisMessageListenerContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }
}
