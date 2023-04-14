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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;

/**
 * @author 박경서 (Kyungseo.Park@gmail.com)
 * @version 1.0
 */
public abstract class DatabaseConfig extends HikariConfig {

    public static final String DATASOURCE1_NAME          = "ds1";
    public static final String REPOSITORY_PACKAGE_PREFIX = "kyungseo.poc.todo.**.repository";
    public static final String ENTITY_PACKAGE_PREFIX     = "kyungseo.poc.todo.**.entity";

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    // 주의! Production 환경인 경우 무조건 'none' 값으로 설정할 것!
    @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
    private String hibernateDdlAuto;

    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String hibernateFormatSql;

    @Value("${spring.jpa.show-sql}")
    private String showSql;

    protected void setConfigureEntityManagerFactory(LocalContainerEntityManagerFactoryBean factory) {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(ImmutableMap.of(
                "hibernate.hbm2ddl.auto", hibernateDdlAuto,
                "hibernate.dialect", hibernateDialect,
                "hibernate.show_sql", showSql,
                "hibernate.format_sql", hibernateFormatSql
        ));
        factory.afterPropertiesSet();
    }

}