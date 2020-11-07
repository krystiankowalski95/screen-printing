package pl.lodz.it.sitodruk.config.datasources;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "pl.lodz.it.sitodruk.repositories.mok",
        entityManagerFactoryRef = "mokEntityManagerFactory",
        transactionManagerRef= "mokTransactionManager"
)
public class MokDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "mok")
    public DataSource mokDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "mokEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mokDataSource())
                .packages("pl.lodz.it.sitodruk.model.mok")
                .build();
    }

    @Primary
    @Bean(name = "mokTransactionManager")
    public PlatformTransactionManager usersTransactionManager(
            final @Qualifier("mokEntityManagerFactory") LocalContainerEntityManagerFactoryBean userEntityManagerFactory) {
        return new JpaTransactionManager(userEntityManagerFactory.getObject());
    }
}
