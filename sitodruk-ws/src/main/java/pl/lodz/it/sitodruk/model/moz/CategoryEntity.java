package pl.lodz.it.sitodruk.model.moz;

import lombok.Data;
import org.hibernate.annotations.Immutable;

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
