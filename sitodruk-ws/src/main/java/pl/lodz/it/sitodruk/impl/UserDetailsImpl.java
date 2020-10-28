package pl.lodz.it.sitodruk.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.lodz.it.sitodruk.model.UserAccessLevelEntity;
import pl.lodz.it.sitodruk.model.UserEntity;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UserEntity user) {
        List<GrantedAuthority> authorities = user.getUserAccessLevelsById().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAccessLevelName()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId()*user.hashCode(),
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
//    package pl.lodz.it.sitodruk.impl;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import pl.lodz.it.sitodruk.model.UserAccessLevelEntity;
//import pl.lodz.it.sitodruk.model.UserEntity;
//
//    @Getter
//    @Setter
//    public class UserDetailsImpl implements UserDetails {
//        private static final long serialVersionUID = 1L;
//        private UserEntity userEntity;
//        @JsonIgnore
//        private Collection<GrantedAuthority> authorities;
//        public UserDetailsImpl(UserEntity userEntity,Collection<GrantedAuthority> authorities) {
//            this.authorities = authorities;
//        }
//
//        public static pl.lodz.it.sitodruk.impl.UserDetailsImpl build(UserEntity user) {
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            for(UserAccessLevelEntity uac: user.getUserAccessLevelsById()){
//                authorities.add(new SimpleGrantedAuthority(uac.getAccessLevelName()));
//            }
//            return new pl.lodz.it.sitodruk.impl.UserDetailsImpl(user,authorities);
//        }
//        public Long getId() {
//            return userEntity.getId()*userEntity.hashCode();
//        }
//
//        public String getEmail() {
//            return userEntity.getEmail();
//        }
//
//        @Override
//        public String getPassword() {
//            return userEntity.getPassword();
//        }
//
//        @Override
//        public String getUsername() {
//            return userEntity.getUsername();
//        }
//
//        @Override
//        public boolean isAccountNonExpired() {
//            return true;
//        }
//
//        @Override
//        public boolean isAccountNonLocked() {
//            return userEntity.isActive();
//        }
//
//        @Override
//        public boolean isCredentialsNonExpired() {
//            return true;
//        }
//
//        @Override
//        public boolean isEnabled() {
//            return userEntity.isConfirmed();
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o)
//                return true;
//            if (o == null || getClass() != o.getClass())
//                return false;
//            pl.lodz.it.sitodruk.impl.UserDetailsImpl user = (pl.lodz.it.sitodruk.impl.UserDetailsImpl) o;
//            return Objects.equals(getId(), user.getId());
//        }
//    }
