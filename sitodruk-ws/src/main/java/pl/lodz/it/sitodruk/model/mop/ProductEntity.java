package pl.lodz.it.sitodruk.model.mop;

import lombok.*;
import pl.lodz.it.sitodruk.model.moz.OrderEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Basic@Column(name = "price", nullable = false, scale = 2)
    private double price;
    @Basic@Column(name = "description", nullable = true, length = 300)
    private String description;
    @Basic
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Basic@Column(name = "category", nullable = false, length = 100)
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
