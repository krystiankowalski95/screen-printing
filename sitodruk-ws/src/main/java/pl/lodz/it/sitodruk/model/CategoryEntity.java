package pl.lodz.it.sitodruk.model;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Immutable
@Table(name = "categories", schema = "public", catalog = "postgres")
public class CategoryEntity {
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "category_name", nullable = false, length = -1)
    private String categoryName;
    @OneToOne
    @JoinColumn(name = "category_name", referencedColumnName = "category_name", nullable = false)
    private ProductEntity productsEntity;

}
