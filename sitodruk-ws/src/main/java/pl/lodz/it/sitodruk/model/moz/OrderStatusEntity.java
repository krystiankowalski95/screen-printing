package pl.lodz.it.sitodruk.model.moz;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Data
@Entity
@Immutable
@Table(name = "order_statuses", schema = "public", catalog = "postgres")
public class OrderStatusEntity {
    @Id@Column(name = "id", nullable = false)
    private long id;
    @Basic@Column(name = "status_name", nullable = false, length = -1)
    private String statusName;
    @OneToOne
    private OrderEntity orderEntity;
}
