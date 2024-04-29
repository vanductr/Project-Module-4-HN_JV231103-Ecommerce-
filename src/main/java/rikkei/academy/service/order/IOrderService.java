package rikkei.academy.service.order;

import rikkei.academy.model.dto.response.OrderResponse;
import rikkei.academy.model.dto.response.UserResponse;
import rikkei.academy.model.entity.Order;
import rikkei.academy.security.principle.UserDetailsCustom;
import rikkei.academy.service.IGenericService;

public interface IOrderService extends IGenericService<Order, Long> {
    OrderResponse getOrderResponse(UserDetailsCustom userDetailsCustom);
}
