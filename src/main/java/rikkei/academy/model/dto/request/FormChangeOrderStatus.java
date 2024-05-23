package rikkei.academy.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rikkei.academy.model.entity.OrderStatusName;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FormChangeOrderStatus {
    private OrderStatusName orderStatusName;
}
