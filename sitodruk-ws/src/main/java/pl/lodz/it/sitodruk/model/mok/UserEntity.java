package pl.lodz.it.sitodruk.model.mok;

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
    private Long id;
    @Basic
    @Column(name = "username", nullable = false, length = 64)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 64)
    private String password;
    @Basic
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Basic
    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed;

    @Basic
    @Column(name = "token", unique = true, length = 32)
    private String token;

    @OneToMany(mappedBy = "loginDataByUserId", cascade = CascadeType.ALL)
    private Collection<UserAccessLevelEntity> userAccessLevelsById = new ArrayList<>();

    @Basic
    @Column(name = "firstname", table = "user_personal_data", nullable = false, length = 64)
    private String firstname;

    @Basic
    @Column(name = "lastname", table = "user_personal_data", nullable = false, length = 100)
    private String lastname;

    @Basic
    @Column(name = "email", table = "user_personal_data", nullable = false, length = 100)
    private String email;
    @Basic
    @Column(name = "phone_number", table = "user_personal_data", nullable = false, length = 15)
    private String phoneNumber;

    @Basic
    @Version
    @Column(name = "version", table = "login_data", nullable = false)
    private Long version;

    @Basic
    @Column(name = "is_token_used")
    private Boolean isTokenExpired;

    @Basic
    @Column(name = "register_lang", length = 4, nullable = true)
    private String registeredLang;

    public UserEntity(){
        this.active = true;
        this.version = 1L;
        this.token = UUID.randomUUID().toString().replace("-", "");
    }

}