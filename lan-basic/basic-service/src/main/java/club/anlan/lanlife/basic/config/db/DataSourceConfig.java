package club.anlan.lanlife.basic.config.db;

import club.anlan.lanlife.component.base.config.BaseConfig;
import club.anlan.lanlife.component.utils.StringUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据源
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/3 22:07
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class DataSourceConfig extends BaseConfig {

    @Override
    public String getConfigPrefix() {
        return "spring.datasource.";
    }

    /**
     * 数据源配置
     */
    @Bean(name = "dataSource")
    public DataSource createDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", getProperty("driverClassName"));
        String url = getProperty("url");
        String schema = getProperty("current-schema");
        if (StringUtil.isNotEmpty(schema)) {
            url += "?currentSchema=" + schema;
        }
        props.put("url", url);
        props.put("username", getProperty("username"));
        props.put("password", getProperty("password"));

        DruidDataSource dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(props);
        setDataSourceExtProps(dataSource);
        return dataSource;
    }

    /**
     * 统一设置DruidDataSource的相关连接属性
     */
    private DataSource setDataSourceExtProps(DruidDataSource dataSource) {
        if (dataSource == null) {
            return null;
        }

        dataSource.setInitialSize(getPropertyInt("initial-size"));
        dataSource.setMinIdle(getPropertyInt("min-idle"));
        dataSource.setMaxActive(getPropertyInt("max-active"));
        dataSource.setMaxWait(getPropertyLong("max-wait"));
        dataSource.setTimeBetweenEvictionRunsMillis(getPropertyLong("time-between-eviction-runs-millis"));
        dataSource.setMinEvictableIdleTimeMillis(getPropertyLong("min-evictable-idle-time-millis"));
        dataSource.setValidationQuery(getProperty("validation-query"));
        dataSource.setTestWhileIdle(getPropertyBoolean("test-while-idle"));
        dataSource.setTestOnBorrow(getPropertyBoolean("test-on-borrow"));
        dataSource.setTestOnReturn(getPropertyBoolean("test-on-return"));
        dataSource.setPoolPreparedStatements(getPropertyBoolean("pool-prepared-statements"));
        dataSource.setConnectionProperties(getProperty("connection-properties"));
        try {
            dataSource.setFilters(getProperty("filters"));
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return dataSource;
    }
}
