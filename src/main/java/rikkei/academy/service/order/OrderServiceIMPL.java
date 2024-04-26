package rikkei.academy.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rikkei.academy.model.dto.response.OrderResponse;
import rikkei.academy.model.dto.response.UserResponse;
import rikkei.academy.model.entity.*;
import rikkei.academy.repository.IOrderDetailRepository;
import rikkei.academy.repository.IOrderRepository;
import rikkei.academy.security.principle.UserDetailsCustom;
import rikkei.academy.service.user.IUserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceIMPL implements IOrderService{
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @Override
    public Order findById(Long id) {
        return null;
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Order save(Order entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public OrderResponse getOrderResponse(UserDetailsCustom userDetailsCustom) {
        User user = userService.findById(userDetailsCustom.getId());
        OrderResponse orderResponse;
        Order order = orderRepository.findFirstByUserOrderByCreatedAtDesc(user);
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrder(order);
        orderResponse = OrderResponse.builder()
                .serialNumber(order.getSerialNumber())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().getOrderStatusName().name())
                .note(order.getNote())
                .receiveName(order.getReceiveName())
                .receiveAddress(order.getReceiveAddress())
                .receivePhone(order.getReceivePhone())
                .createdAt(order.getCreatedAt())
                .receivedAt(order.getReceivedAt())
                .build();
        Map<String, Integer> map = new HashMap<>();
        for (OrderDetail orderDetail : orderDetailList) {
            map.put(orderDetail.getProduct().getProductName(), orderDetail.getOrderQuantity());
        }
        orderResponse.setOrderItem(map);
        return orderResponse;
    }
}
