package rikkei.academy.model.dto.response;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductResponse {
    private String sku;

    private String productName;

    private String description;

    private Double unitPrice;

    private Integer stockQuantity;

    private String image;

    private String category;

    private Date createdAt;

    private Date updatedAt;
}
