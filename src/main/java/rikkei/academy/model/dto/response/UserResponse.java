package rikkei.academy.model.dto.response;

import lombok.*;
import rikkei.academy.model.entity.Role;
import rikkei.academy.model.entity.RoleName;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {
    private String username;

    private String email;

    private String fullName;

    private Boolean status;

    private String avatar;

    private String phone;

    private String address;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Boolean isDeleted;

    private Set<RoleName> roleSet;
}
