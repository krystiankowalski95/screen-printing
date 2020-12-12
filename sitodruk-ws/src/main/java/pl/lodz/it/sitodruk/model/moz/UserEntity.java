package pl.lodz.it.sitodruk.model.moz;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@Table(name = "login_data")
@SecondaryTables({
        @SecondaryTable(name = "user_personal_data", pkJoinColumns = {
                @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")})
})
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "u_generator")
    @SequenceGenerator(name = "u_generator", sequenceName = "login_data_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "username", nullable = false, length = 64)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 64)
    private String password;
    @Basic
    @Column(name = "active", nullable = false)
    private boolean active;

    @Basic
    @Column(name = "confirmed", nullable = false)
    private boolean confirmed;

    @Basic
    @Column(name = "token", unique = true)
    private String token;

    @OneToMany(mappedBy = "loginDataByUserId", cascade = CascadeType.ALL)
    private Collection<UserAccessLevelEntity> userAccessLevelsById = new ArrayList<>();

    @Basic
    @Column(name = "firstname", table = "user_personal_data", nullable = false, length = 64)
    private String firstname;

    @Basic
    @Column(name = "lastname", table = "user_personal_data", nullable = false, length = 64)
    private String lastname;

    @Basic
    @Column(name = "email", table = "user_personal_data", nullable = false, length = 64)
    private String email;
    @Basic
    @Column(name = "phone_number", table = "user_personal_data", nullable = false, length = 15)
    private String phoneNumber;

    @OneToMany(mappedBy = "userEntity")
    private Collection<AddressEntity> addresses = new ArrayList<>();


    @Basic
    @Version
    @Column(name = "version", table = "user_personal_data", nullable = false)
    private Long version;

    public UserEntity(){
        this.active = true;
        this.token = UUID.randomUUID().toString().replace("-", "");
    }

}