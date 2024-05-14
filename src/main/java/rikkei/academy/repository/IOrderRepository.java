package rikkei.academy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rikkei.academy.model.entity.Order;
import rikkei.academy.model.entity.OrderStatusName;
import rikkei.academy.model.entity.User;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Order findFirstByUserOrderByCreatedAtDesc(User user);

    Page<Order> findByStatusOrderStatusName(OrderStatusName status, Pageable pageable);

    Page<Order> findByUser(User user, Pageable pageable);

    Order findBySerialNumber(String serialNumber);

    Page<Order> findByUserAndStatusOrderStatusName(User user, OrderStatusName status, Pageable pageable);
}
