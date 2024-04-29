package rikkei.academy.model.dto.response;

import lombok.*;
import rikkei.academy.model.entity.OrderStatus;
import rikkei.academy.model.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrderResponse {
    private String serialNumber;

    private BigDecimal totalPrice;

    private String status;

    private String note;

    private String receiveName;

    private String receiveAddress;

    private String receivePhone;

    private LocalDate createdAt;

    private LocalDate receivedAt;

    private Map<String, Integer> orderItem;
}
