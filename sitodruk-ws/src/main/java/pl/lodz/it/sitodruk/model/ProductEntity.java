package pl.lodz.it.sitodruk.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "products", schema = "public", catalog = "postgres")
public class ProductEntity {
    @Id@Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name = "product_generator", sequenceName = "products_seq", allocationSize = 1)
    private long id;
    @Basic@Column(name = "name", nullable = false, length = 200)
    private String name;
    @Basic@Column(name = "price", nullable = false, precision = 0)
    private double price;
    @Basic
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Basic@Column(name = "category_name", nullable = false, length = -1)
    private String categoryName;
    @Basic@Column(name = "version", nullable = false)
    private long version;
    @OneToOne(mappedBy = "productsEntity")
    private CategoryEntity categoryEntity;
}
