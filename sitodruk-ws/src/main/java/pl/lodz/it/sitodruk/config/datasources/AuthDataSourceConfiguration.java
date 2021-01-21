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
@EnableJpaRepositories(basePackages = "pl.lodz.it.sitodruk.repositories.auth",
        entityManagerFactoryRef = "authEntityManagerFactory",
        transactionManagerRef= "authTransactionManager"
)
public class AuthDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "auth")
    public DataSource authDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "authEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean authUserEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(authDataSource())
                .packages("pl.lodz.it.sitodruk.model.mok")
                .build();
    }

    @Bean(name = "authTransactionManager")
    public PlatformTransactionManager usersTransactionManager(
            final @Qualifier("authEntityManagerFactory") LocalContainerEntityManagerFactoryBean authUserEntityManagerFactory) {
        return new JpaTransactionManager(authUserEntityManagerFactory.getObject());
    }
}