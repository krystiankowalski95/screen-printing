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
@EnableJpaRepositories(basePackages = "pl.lodz.it.sitodruk.repositories.mop",
        entityManagerFactoryRef = "mopEntityManagerFactory",
        transactionManagerRef= "mopTransactionManager"
)
public class MopDataSourceConfiguration {


    @Bean
    @ConfigurationProperties(prefix = "mop")
    public DataSource mopDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mopEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean productsEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mopDataSource())
                .packages("pl.lodz.it.sitodruk.model.mop")
                .build();
    }

    @Bean(name = "mopTransactionManager")
    public PlatformTransactionManager productsTransactionManager(
            final @Qualifier("mopEntityManagerFactory") LocalContainerEntityManagerFactoryBean productsEntityManagerFactory) {
        return new JpaTransactionManager(productsEntityManagerFactory.getObject());
    }
}
