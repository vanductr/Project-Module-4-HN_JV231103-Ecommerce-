package rikkei.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rikkei.academy.model.entity.Order;
import rikkei.academy.model.entity.User;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Order findFirstByUserOrderByCreatedAtDesc(User user);
}
