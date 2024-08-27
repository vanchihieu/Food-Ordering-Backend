package vn.edu.iuh.fit.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.food.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}