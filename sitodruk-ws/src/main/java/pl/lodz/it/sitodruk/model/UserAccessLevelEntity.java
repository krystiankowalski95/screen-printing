package pl.lodz.it.sitodruk.model;

import lombok.*;
import org.apache.catalina.User;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "user_access_level", schema = "public", catalog = "postgres")
public class UserAccessLevelEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_level_generator")
    @SequenceGenerator(name = "access_level_generator", sequenceName = "user_access_level_seq", allocationSize = 1)
    private int id;
    @Basic
    @Column(name = "access_level_name", nullable = false, length = 64)
    private String accessLevelName;
    @Basic
    @Column(name = "active", nullable = false)
    private boolean active;
    @Basic
    @Column(name = "version", nullable = false)
    @Version
    private long version;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity loginDataByUserId;
}
