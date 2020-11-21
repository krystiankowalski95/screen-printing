package pl.lodz.it.sitodruk.model.moz;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

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
    @Basic@Column(name = "version", nullable = false)
    private int version;
    @Basic@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private String userId;
    @ManyToOne@JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    private AddressEntity addressByAddressId;
    @Basic@Column(name = "order_status", nullable = false, length = -1)
    private String order;

}
