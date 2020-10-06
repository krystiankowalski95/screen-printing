package pl.lodz.it.sitodruk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "mok")
    public DataSource modulObslugiKontDataSource() {
        return DataSourceBuilder.create().build();
    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean mokEntityManagerFactory(
//            EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource(modulObslugiKontDataSource())
//                .persistenceUnit("mok")
//                .build();
//    }
}
