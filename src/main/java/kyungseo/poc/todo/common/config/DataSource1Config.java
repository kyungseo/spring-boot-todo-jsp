/* ============================================================================
 * KYUNGSEO.PoC > Development Templates for building Web Apps
 *
 * Copyright 2023 Kyungseo Park <Kyungseo.Park@gmail.com>
 * ----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================================= */

package kyungseo.poc.todo.common.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @author 박경서 (Kyungseo.Park@gmail.com)
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = DatabaseConfig.DATASOURCE1_NAME + "EntityManagerFactory",
        transactionManagerRef = DatabaseConfig.DATASOURCE1_NAME + "TransactionManager",
        // ex) "kyungseo.poc.todo.**.persistence.repository"
        basePackages = { DatabaseConfig.REPOSITORY_PACKAGE_PREFIX }
    )
public class DataSource1Config extends DatabaseConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    // ex) "kyungseo.poc.todo.**.persistence.entity"
    private final String[] ENTITY_TO_SCAN = new String[] { DatabaseConfig.ENTITY_PACKAGE_PREFIX };

    @Value("${spring.datasource.ds1.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.ds1.jdbc-url}")
    private String jdbcUrl;

    @Value("${spring.datasource.ds1.username}")
    private String userName;

    @Value("${spring.datasource.ds1.password}")
    private String password;

    // DataSource =========================================
    @Bean(name = DatabaseConfig.DATASOURCE1_NAME + "DataSource")
    @Primary
    public DataSource ds1DataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    // JPA ================================================
    @Bean(name = DatabaseConfig.DATASOURCE1_NAME + "EntityManagerFactory")
    @Primary
    public EntityManagerFactory entityManagerFactory(
            @Qualifier(DatabaseConfig.DATASOURCE1_NAME + "DataSource") DataSource dataSource) {
       LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
       em.setDataSource(dataSource);
       em.setPackagesToScan(this.ENTITY_TO_SCAN);
       em.setPersistenceUnitName(DatabaseConfig.DATASOURCE1_NAME + "PersistanceUnit");
       setConfigureEntityManagerFactory(em);

       return em.getObject();
    }

    @Bean(name = DatabaseConfig.DATASOURCE1_NAME + "TransactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(
            @Qualifier(DatabaseConfig.DATASOURCE1_NAME + "EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
