package net.demo.modules.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

import net.demo.modules.services.db.QueryTemplate;

/**
 * HikariCP/JDBC Template Connection Pool Bean 생성
 * @author		HyeonSu Jeon
 * @version		1.1
 * @since		1.0
 */
@Configuration
public class HikariCPConfig {
	/**
	 * application.properties
	 */
	@Autowired
	private Environment env;
	
	/**
	 * QueryTemplate class injection object
	 */
	@Autowired
	private QueryTemplate queryTemplate;
	
	@Value("${db.cpool.setMinimumIdle}")
    Integer minimumIdle;
	
	@Value("${db.cpool.setMaximumPoolSize}")
	Integer maximumPoolSize;
	
    /**
     * constructor
     */
    public HikariCPConfig() {
        super();
    }
    
	/**
	 * HikariCP Connection Pool 설정
	 * 
	 * @return HikariCP 설정된 데이터소스 반환
	 */
	@Bean(name="DataSource")
	public DataSource dataSource(){
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setConnectionTestQuery("SELECT 1 AS test");
        
        if (minimumIdle == null) dataSource.setMinimumIdle(5);
        else dataSource.setMinimumIdle(minimumIdle);
        
        if (maximumPoolSize == null) dataSource.setMaximumPoolSize(10);
        else dataSource.setMaximumPoolSize(maximumPoolSize);
        
        dataSource.setPoolName("ctiasHikariCP");

        dataSource.addDataSourceProperty("dataSource.cachePrepStmts", 			"true");
        dataSource.addDataSourceProperty("dataSource.prepStmtCacheSize", 		"250");
        dataSource.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit",	"2048");
        dataSource.addDataSourceProperty("dataSource.useServerPrepStmts", 		"true");
        
		return dataSource;
	}
	
    /**
     * JdbcTemplate HikariCP 연동 설정
     * 
     * @param  dataSource 	HikariCP Datasource
     * @return JdbcTemplate 객체 반환
     */
    @Bean(name = "JdbcTemplate")
    public JdbcTemplate outerJdbcTemplate(@Qualifier("DataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    
	
	/**
	 * AgensGraph by JdbcTemplate
	 * 
	 * @param	variable	Description text
	 * @throws	exception_name	Description
	 * @return	Description text
	 */
	@Bean(name="QueryTemplate")
	public QueryTemplate createQueryTemplate() {
		return queryTemplate;
	}
}
