package rikkei.academy.security.principle;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rikkei.academy.model.entity.User;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserDetailsCustom implements UserDetails {
    private Long id;

    private String username;

    private String fullName;

    private String password;

    private String email;

    private boolean status;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsCustom build(User user) {
        List<? extends GrantedAuthority> authorityList = user.getRoleSet().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).toList();
        return UserDetailsCustom.builder()
                .id(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .status(user.getStatus())
                .authorities(authorityList)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
}
