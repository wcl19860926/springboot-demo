package com.study.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DruidDataSourceConfig {

    private Logger  logger  = LoggerFactory.getLogger(DruidDataSourceConfig.class);


    @Autowired
    private DruidDataSourceProperties dataSourceProperties;


    @Bean
    public DataSource dataSource() throws  Exception{

        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dataSourceProperties.getUrl());
        datasource.setDriverClassName(dataSourceProperties.getDriverClassName());
        datasource.setUsername(dataSourceProperties.getUsername());
        datasource.setPassword(dataSourceProperties.getPassword());
        datasource.setInitialSize(dataSourceProperties.getInitialSize());
        datasource.setMinIdle(dataSourceProperties.getMinIdle());
        datasource.setMaxWait(dataSourceProperties.getMaxWait());
        datasource.setMaxActive(dataSourceProperties.getMaxActive());
        datasource.setMinEvictableIdleTimeMillis(
                dataSourceProperties.getMinEvictableIdleTimeMillis());
        try {
            datasource.setFilters(dataSourceProperties.getFilters());
        } catch (Exception e ){
            logger.error("初始化数据源失败！", e );
            throw    new Exception("初始化数据源失败！" ,e);
        }
        return datasource;
    }


   /* @Bean(name = "transactionManager")
    public DataSourceTransactionManager dbOneTransactionManager(
            @Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name = "sqlSessionFactory")
    @ConditionalOnMissingBean(name = "sqlSessionFactory")
    public SqlSessionFactory dbOneSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:myppaer/*Mapper.xml"));
        return sessionFactory.getObject();
    }
*/


}