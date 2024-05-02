package rikkei.academy.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormEditUserRequest {
    private String email;

    private String fullName;

    private String avatar;

    private String phone;

    private String address;
}
