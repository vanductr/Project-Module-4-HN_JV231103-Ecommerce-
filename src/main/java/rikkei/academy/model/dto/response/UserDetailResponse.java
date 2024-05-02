package rikkei.academy.model.dto.response;


import lombok.*;

import java.time.LocalDate;

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
}
