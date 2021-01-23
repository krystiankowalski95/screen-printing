package pl.lodz.it.sitodruk.model.moz;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders", schema = "public", catalog = "postgres")
public class OrderEntity {
    @Id@Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name = "order_generator", sequenceName = "orders_seq", allocationSize = 1)
    private Long id;
    @Basic@Column(name = "payu_order_id", nullable = false)
    private String payUOrderId;
    @Basic@Column(name = "timestamp", nullable = true)
    private LocalDateTime timestamp = LocalDateTime.now();
    @Version
    @Basic@Column(name = "version", nullable = false)
    private Long version;
    @Basic@Column(name = "username", nullable = false)
    private String username;
    @Basic@Column(name = "total_value", nullable = false)
    private Double totalValue;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    private AddressEntity addressByAddressId;
    @ManyToOne
    @JoinColumn(name = "order_status", referencedColumnName = "status_name" ,nullable = false)
    private OrderStatusEntity orderStatus;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private List<OrderProductEntity> orderProductEntities = new ArrayList<>();

    public OrderEntity(){

    }

}
