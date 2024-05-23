package rikkei.academy.service.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.response.OrderResponse;
import rikkei.academy.model.dto.response.OrderResponseRoleAdmin;
import rikkei.academy.model.entity.Order;
import rikkei.academy.model.entity.OrderStatusName;
import rikkei.academy.security.principle.UserDetailsCustom;
import rikkei.academy.service.IGenericService;

import java.util.List;


public interface IOrderService extends IGenericService<Order, Long> {
    OrderResponse getOrderResponse(UserDetailsCustom userDetailsCustom);

    Page<OrderResponseRoleAdmin> getAllOrderRoleAdmin(Pageable pageable);

    Page<OrderResponseRoleAdmin> findByStatusOrderStatusName(String status, Pageable pageable) throws DataExistException;

    OrderResponseRoleAdmin getOrderById(Long id);

    OrderResponseRoleAdmin updateOrderStatusById(Long orderId, OrderStatusName status);

    List<OrderResponse> findByUser(UserDetailsCustom userDetailsCustom, Pageable pageable) throws DataExistException;

    OrderResponse findBySerialNumber(String serialNumber, UserDetailsCustom userDetailsCustom);

    List<OrderResponse> findByUserAndStatusOrderStatusName(UserDetailsCustom userDetailsCustom, String status, Pageable pageable) throws DataExistException;
}
