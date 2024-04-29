package rikkei.academy.service.orderstatus;

import rikkei.academy.model.entity.OrderStatus;
import rikkei.academy.model.entity.OrderStatusName;
import rikkei.academy.service.IGenericService;

public interface IOrderStatusService extends IGenericService<OrderStatus, Long> {
    OrderStatus findByOrderStatusName(OrderStatusName statusName);
}
