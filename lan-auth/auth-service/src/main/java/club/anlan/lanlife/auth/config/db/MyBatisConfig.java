//package club.anlan.lanlife.auth.config.db;
//
//import club.anlan.lanlife.component.base.config.BaseConfig;
//import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
//import com.baomidou.mybatisplus.core.config.GlobalConfig;
//import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.DefaultResourceLoader;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
///**
// * MyBatisConfig
// *
// * @author lan
// * @version 1.0
// * @date 2021/4/5 19:50
// */
//@Configuration
//@EnableTransactionManagement
//@Slf4j
//public class MyBatisConfig extends BaseConfig {
//
//    @Override
//    public String getConfigPrefix() {
//        return "spring.mybatis.";
//    }
//
//    /**
//     * 根据数据源创建SqlSessionFactory
//     */
//    @Bean(name = "sqlSessionFactory")
//    @DependsOn({ "dataSource" })
//    @Primary
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
//        log.info("SqlSessionFactory init start ..");
//
//        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//
//        // 设置全局的配置：id默认自动；MP默认的id生成规则为ID_WORKER
//        // GlobalConfiguration globalConfig = new GlobalConfiguration();
//        // globalConfig.setIdType(IdType.INPUT.getKey());
//        // sqlSessionFactoryBean.setGlobalConfig(globalConfig);
//        // 以下两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不用加
//        // 指定基包
//        sqlSessionFactoryBean.setTypeAliasesPackage(getProperty("type-aliases-package"));
//
//        // 指定xml文件位置
//        Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
//                .getResources(getProperty("mapper-locations"));
//        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
//        // 指定配置xml位置
//        Resource configResource = new DefaultResourceLoader().getResource(getProperty("config-location"));
//        sqlSessionFactoryBean.setConfigLocation(configResource);
//        GlobalConfig globalConfig = new GlobalConfig();
//        globalConfig.setSqlInjector(new DefaultSqlInjector());
//        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
//        // 配置vfs，否则实体别名配置无效
//        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
//        return sqlSessionFactoryBean.getObject();
//    }
//}
