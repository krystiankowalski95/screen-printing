package pl.lodz.it.sitodruk.model.mop;

import lombok.*;
import org.hibernate.annotations.Immutable;
import pl.lodz.it.sitodruk.model.mop.ProductEntity;

import javax.persistence.*;

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
}
