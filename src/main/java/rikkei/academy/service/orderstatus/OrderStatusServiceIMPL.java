package rikkei.academy.service.orderstatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rikkei.academy.model.entity.OrderStatus;
import rikkei.academy.model.entity.OrderStatusName;
import rikkei.academy.repository.IOrderStatusRepository;

@Service
public class OrderStatusServiceIMPL implements IOrderStatusService{
    @Autowired
    private IOrderStatusRepository orderStatusRepository;

    @Override
    public OrderStatus findById(Long id) {
        return null;
    }

    @Override
    public Page<OrderStatus> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public OrderStatus save(OrderStatus entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public OrderStatus findByOrderStatusName(OrderStatusName statusName) {
        return orderStatusRepository.findByOrderStatusName(statusName);
    }
}
