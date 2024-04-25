package rikkei.academy.model.dto.response;

import lombok.*;
import rikkei.academy.model.entity.RoleName;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RoleResponse {
    private Set<RoleName> roleNameSet;
}
