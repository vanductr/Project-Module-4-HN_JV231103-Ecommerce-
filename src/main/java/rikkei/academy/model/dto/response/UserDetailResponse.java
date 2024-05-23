package rikkei.academy.model.dto.response;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import rikkei.academy.model.entity.Role;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserDetailResponse {
    private String username;

    private String email;

    private String fullName;

    private String avatar;

    private String phone;

    private String address;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Collection<? extends GrantedAuthority> roleSet;
}
