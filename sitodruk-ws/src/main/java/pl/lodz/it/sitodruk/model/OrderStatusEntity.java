package pl.lodz.it.sitodruk.model;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.Objects;

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
    @JoinColumn(name = "status_name", referencedColumnName = "order_status", nullable = false)
    private OrderEntity orderEntity;
}
