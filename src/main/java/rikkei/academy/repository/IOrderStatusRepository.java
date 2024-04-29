package rikkei.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rikkei.academy.model.entity.OrderStatus;
import rikkei.academy.model.entity.OrderStatusName;

import java.util.Optional;

@Repository
public interface IOrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    OrderStatus findByOrderStatusName(OrderStatusName statusName);
}
