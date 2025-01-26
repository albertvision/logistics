package bg.nbu.cscb532.logistics.data.entity;

import bg.nbu.cscb532.logistics.data.enumeration.Authority;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.aspectj.lang.annotation.After;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity implements UserDetails {
    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    private String name;

    private String phone;

    @OneToMany(mappedBy = "sender")
    private Set<Shipping> sentShippings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "receiver")
    private Set<Shipping> receivedShippings = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }
}
