package pl.lodz.it.sitodruk.config.datasources;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "pl.lodz.it.sitodruk.repositories.moz",
        entityManagerFactoryRef = "mozEntityManagerFactory",
        transactionManagerRef= "mozTransactionManager"
)
public class MozDataSourceConfiguration {


    @Bean
    @ConfigurationProperties(prefix = "moz")
    public DataSource mozDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mozEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean ordersEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mozDataSource())
                .packages("pl.lodz.it.sitodruk.model.moz")
                .build();
    }

    @Bean(name = "mozTransactionManager")
    public PlatformTransactionManager productsTransactionManager(
            final @Qualifier("mozEntityManagerFactory") LocalContainerEntityManagerFactoryBean ordersEntityManagerFactory) {
        return new JpaTransactionManager(ordersEntityManagerFactory.getObject());
    }
}
