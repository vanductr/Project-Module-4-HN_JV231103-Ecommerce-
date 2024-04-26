package rikkei.academy.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormProductRequest {
    @NotBlank
    private String productName;

    @NotBlank
    @Size(max = 100)
    private String description;

    @NonNull
    private Double unitPrice;

    @NotNull
    private Integer stockQuantity;

    private String image;

    @NotNull
    private Long category;
}
