package com.pagehelper;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author hb
 * @description 启动
 * @create 2018-07-18-上午11:09
 **/

@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
@EnableScheduling
@ServletComponentScan("com.pagehelper")
@MapperScan("com.pagehelper.domain.mapper")
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource() {
		return new com.alibaba.druid.pool.DruidDataSource();
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		sqlSessionFactoryBean.setTypeAliasesPackage("com.pagehelper.domain.model");

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
		sqlSessionFactoryBean.setConfigLocation(resolver.getResources("classpath:mybatis-config.xml")[0]);
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		logger.info("SpringBoot Start Success");
	}
}
