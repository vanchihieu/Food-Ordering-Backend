package vn.edu.iuh.fit.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.food.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
