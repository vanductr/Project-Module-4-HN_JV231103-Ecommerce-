package rikkei.academy.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.response.OrderResponse;
import rikkei.academy.model.dto.response.OrderResponseRoleAdmin;
import rikkei.academy.model.dto.response.UserResponse;
import rikkei.academy.model.entity.*;
import rikkei.academy.repository.IOrderDetailRepository;
import rikkei.academy.repository.IOrderRepository;
import rikkei.academy.repository.IOrderStatusRepository;
import rikkei.academy.security.principle.UserDetailsCustom;
import rikkei.academy.service.user.IUserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class OrderServiceIMPL implements IOrderService{
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @Autowired
    private IOrderStatusRepository orderStatusRepository;

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy tài nguyên"));
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
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

    @Override
    public Page<OrderResponseRoleAdmin> getAllOrderRoleAdmin(Pageable pageable) {
        Page<Order> orderPage = findAll(pageable);
        return getOrderResponseRoleAdmins(pageable, orderPage);
    }

    @Override
    public Page<OrderResponseRoleAdmin> findByStatusOrderStatusName(String status, Pageable pageable) throws DataExistException {
        OrderStatusName orderStatusName = switch (status) {
            case "WAITING" -> OrderStatusName.WAITING;
            case "CONFIRM" -> OrderStatusName.CONFIRM;
            case "DELIVERY" -> OrderStatusName.DELIVERY;
            case "SUCCESS" -> OrderStatusName.SUCCESS;
            case "CANCEL" -> OrderStatusName.CANCEL;
            case "DENIED" -> OrderStatusName.DENIED;
            default -> throw new DataExistException("Yêu cầu không hợp lệ, hãy kiểm tra lại", "Lỗi");
        };
        Page<Order> orderRepositoryByStatusOrderStatusName = orderRepository.findByStatusOrderStatusName(orderStatusName, pageable);

        return getOrderResponseRoleAdmins(pageable, orderRepositoryByStatusOrderStatusName);
    }

    private Page<OrderResponseRoleAdmin> getOrderResponseRoleAdmins(Pageable pageable, Page<Order> orderRepositoryByStatusOrderStatusName) {
        List<OrderResponseRoleAdmin> orderResponseRoleAdmins = new ArrayList<>();
        for (Order order : orderRepositoryByStatusOrderStatusName) {
            OrderResponseRoleAdmin orderResponseRoleAdmin = OrderResponseRoleAdmin.builder()
                    .status(order.getStatus().getOrderStatusName().name())
                    .serialNumber(order.getSerialNumber())
                    .receiveAddress(order.getReceiveAddress())
                    .totalPrice(order.getTotalPrice())
                    .createdAt(order.getCreatedAt())
                    .note(order.getNote())
                    .receivedAt(order.getReceivedAt())
                    .receiveName(order.getReceiveName())
                    .user(order.getUser().getFullName())
                    .receivePhone(order.getReceivePhone())
                    .build();
            orderResponseRoleAdmins.add(orderResponseRoleAdmin);
        }
        return new PageImpl<>(orderResponseRoleAdmins, pageable, orderRepositoryByStatusOrderStatusName.getTotalElements());
    }

    @Override
    public OrderResponseRoleAdmin getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại tài nguyên"));
        return OrderResponseRoleAdmin.builder()
                .status(order.getStatus().getOrderStatusName().name())
                .serialNumber(order.getSerialNumber())
                .receiveAddress(order.getReceiveAddress())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .note(order.getNote())
                .receivedAt(order.getReceivedAt())
                .receiveName(order.getReceiveName())
                .user(order.getUser().getFullName())
                .receivePhone(order.getReceivePhone())
                .build();
    }

    @Override
    public OrderResponseRoleAdmin updateOrderStatusById(Long orderId, OrderStatusName status) {
        Order order = findById(orderId);
        OrderStatus orderStatus = orderStatusRepository.findByOrderStatusName(status);
        order.setStatus(orderStatus);
        orderRepository.save(order);
        return OrderResponseRoleAdmin.builder()
                .status(order.getStatus().getOrderStatusName().name())
                .serialNumber(order.getSerialNumber())
                .receiveAddress(order.getReceiveAddress())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .note(order.getNote())
                .receivedAt(order.getReceivedAt())
                .receiveName(order.getReceiveName())
                .user(order.getUser().getFullName())
                .receivePhone(order.getReceivePhone())
                .build();
    }

    @Override
    public List<OrderResponse> findByUser(UserDetailsCustom userDetailsCustom, Pageable pageable) throws DataExistException {
        User user = userService.findById(userDetailsCustom.getId());
        Page<Order> orderPage = orderRepository.findByUser(user, pageable);
        if (!orderPage.hasContent()) {
            throw new DataExistException("Lịch sử mua hàng của bạn đang trống", "Lỗi");
        }
        List<OrderResponse> orderResponseList = new ArrayList<>();
        for (Order order : orderPage) {
            OrderResponse orderResponse = OrderResponse.builder()
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
            orderResponseList.add(orderResponse);
        }
        return orderResponseList;
    }

    @Override
    public OrderResponse findBySerialNumber(String serialNumber, UserDetailsCustom userDetailsCustom) {
        Order order = orderRepository.findBySerialNumber(serialNumber);
        if (order == null || !order.getUser().equals(userService.findById(userDetailsCustom.getId()))) {
            throw new NoSuchElementException("Không tồn tại tài nguyên!");
        }
        return OrderResponse.builder()
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
    }

    @Override
    public List<OrderResponse> findByUserAndStatusOrderStatusName(UserDetailsCustom userDetailsCustom, String status, Pageable pageable) throws DataExistException {
        OrderStatusName orderStatusName = switch (status) {
            case "WAITING" -> OrderStatusName.WAITING;
            case "CONFIRM" -> OrderStatusName.CONFIRM;
            case "DELIVERY" -> OrderStatusName.DELIVERY;
            case "SUCCESS" -> OrderStatusName.SUCCESS;
            case "CANCEL" -> OrderStatusName.CANCEL;
            case "DENIED" -> OrderStatusName.DENIED;
            default -> throw new DataExistException("Yêu cầu không hợp lệ, hãy kiểm tra lại", "Lỗi");
        };
        Page<Order> orderPage = orderRepository.findByUserAndStatusOrderStatusName(userService.findById(userDetailsCustom.getId()), orderStatusName, pageable);
        if (!orderPage.hasContent()) {
            throw new DataExistException("Lịch sử mua hàng của bạn đang trống", "Lỗi");
        }
        List<OrderResponse> orderResponseList = new ArrayList<>();
        for (Order order : orderPage) {
            OrderResponse orderResponse = OrderResponse.builder()
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
            orderResponseList.add(orderResponse);
        }
        return orderResponseList;
    }
}
