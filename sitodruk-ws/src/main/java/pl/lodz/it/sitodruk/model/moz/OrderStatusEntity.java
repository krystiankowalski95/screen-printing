package pl.lodz.it.sitodruk.model.moz;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Immutable
@Table(name = "order_statuses", schema = "public", catalog = "postgres")
public class OrderStatusEntity implements Serializable {
    @Id@Column(name = "id", nullable = false)
    private Long id;
    @Basic@Column(name = "status_name", nullable = false, length = 100)
    private String statusName;
}
