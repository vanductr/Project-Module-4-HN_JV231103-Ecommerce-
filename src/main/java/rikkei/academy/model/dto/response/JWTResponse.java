package rikkei.academy.model.dto.response;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import rikkei.academy.model.entity.Role;

import java.util.Collection;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class JWTResponse {
    private final String type = "Bearer";

    private String accessToken;

    private String fullName;

    private String email;

    private Collection<? extends GrantedAuthority> roleSet;

    private boolean status;
}
