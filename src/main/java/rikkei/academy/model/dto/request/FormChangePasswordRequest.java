package rikkei.academy.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormChangePasswordRequest {
    private String oldPass;

    private String newPass;

    private String confirmNewPass;
}
