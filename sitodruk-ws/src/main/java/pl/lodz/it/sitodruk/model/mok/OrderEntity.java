package pl.lodz.it.sitodruk.model.mok;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "orders", schema = "public", catalog = "postgres")
public class OrderEntity {
    @Id@Column(name = "id", nullable = false)
    private long id;
    @Basic@Column(name = "timestamp", nullable = true)
    private Date timestamp;
    @Basic@Column(name = "version", nullable = false)
    private int version;
    @ManyToOne@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity loginDataByUserId;
    @ManyToOne@JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    private AddressEntity addressByAddressId;
    @OneToOne
    private OrderEntity orderEntity;

}
