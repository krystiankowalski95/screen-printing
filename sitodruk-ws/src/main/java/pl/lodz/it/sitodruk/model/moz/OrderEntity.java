package pl.lodz.it.sitodruk.model.moz;

import lombok.*;
import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders", schema = "public", catalog = "postgres")
public class OrderEntity {
    @Id@Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name = "order_generator", sequenceName = "orders_seq", allocationSize = 1)
    private long id;
    @Basic@Column(name = "timestamp", nullable = true)
    private Date timestamp;
    @Version
    @Basic@Column(name = "version", nullable = false)
    private Long version;
    @Basic@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private String userId;
    @ManyToOne@JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    private AddressEntity addressByAddressId;
    @Basic
    @JoinColumn(name = "order_status", referencedColumnName = "status_name" ,nullable = false)
    private String orderStatus;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "order_product",
            joinColumns = { @JoinColumn(name = "order_id") },
            inverseJoinColumns = { @JoinColumn(name = "product_id")}
    )
    private List<ProductEntity> productEntityList = new ArrayList<>();

}
