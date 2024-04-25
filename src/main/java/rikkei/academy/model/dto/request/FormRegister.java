package rikkei.academy.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormRegister {
    private String username;

    private String email;

    private String fullName;

    private String password;

    private String avatar;

    private String phone;

    private String address;

    private Date createdAt;

    private Date updatedAt;

    private Set<String> roles;
}
