package pl.lodz.it.sitodruk.model.moz;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "order_product" , schema = "public", catalog = "postgres")
public class OrderProductEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_product_generator")
    @SequenceGenerator(name = "order_product_generator", sequenceName = "order_product_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @Column(name = "amount")
    private Integer amount;
}
