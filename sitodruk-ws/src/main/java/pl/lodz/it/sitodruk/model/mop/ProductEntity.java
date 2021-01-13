package pl.lodz.it.sitodruk.model.mop;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products", schema = "public", catalog = "postgres")
public class ProductEntity {
    @Id@Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name = "product_generator", sequenceName = "products_seq", allocationSize = 1)
    private Long id;
    @Basic@Column(name = "name", nullable = false, length = 200)
    private String name;
    @Basic@Column(name = "price", nullable = false, precision = 0)
    private Double price;
    @Basic
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Basic@Column(name = "category", nullable = false, length = -1)
    private String categoryName;
    @Basic@Column(name = "stock", nullable = false)
    private  Long stock;
    @Version
    @Basic@Column(name = "version", nullable = false)
    private Long version;

    public ProductEntity(){
        this.version = 1L;
    }
}
