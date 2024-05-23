package rikkei.academy.model.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class OrderResponseRoleAdmin {
    private String serialNumber;

    private String user;

    private BigDecimal totalPrice;

    private String status;

    private String note;

    private String receiveName;

    private String receiveAddress;

    private String receivePhone;

    private LocalDate createdAt;

    private LocalDate receivedAt;
}
