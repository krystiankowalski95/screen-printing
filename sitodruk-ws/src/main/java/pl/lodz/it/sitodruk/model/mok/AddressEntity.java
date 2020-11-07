package pl.lodz.it.sitodruk.model.mok;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "address", schema = "public", catalog = "postgres")
public class AddressEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_generator")
    @SequenceGenerator(name = "address_generator", sequenceName = "address_seq", allocationSize = 1)
    private long id;
    @Basic
    @Column(name = "country", nullable = false, length = -1)
    private String country;
    @Basic
    @Column(name = "voivodeship", nullable = false, length = -1)
    private String voivodeship;
    @Basic
    @Column(name = "city", nullable = false, length = -1)
    private String city;
    @Basic
    @Column(name = "postal_code", nullable = false, length = -1)
    private String postalCode;
    @Basic
    @Column(name = "street", nullable = false, length = -1)
    private String street;
    @Basic
    @Column(name = "street_number", nullable = false, length = -1)
    private String streetNumber;
    @Basic
    @Column(name = "version", nullable = false)
    private long version;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userEntity;
    @OneToMany(mappedBy = "addressByAddressId")
    private Collection<OrderEntity> orderById;

}
