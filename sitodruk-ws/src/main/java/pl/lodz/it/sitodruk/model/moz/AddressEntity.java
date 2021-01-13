package pl.lodz.it.sitodruk.model.moz;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Table(name = "address", schema = "public", catalog = "postgres")
public class AddressEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_generator")
    @SequenceGenerator(name = "address_generator", sequenceName = "address_seq", allocationSize = 1)
    private Long id;
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
    @Column(name = "version", nullable = false)
    @Version
    private Long version;
    @OneToMany(mappedBy = "addressByAddressId")
    private Collection<OrderEntity> orderById = new ArrayList<>();

    public AddressEntity(){
        this.version = 1L;
    }

}
