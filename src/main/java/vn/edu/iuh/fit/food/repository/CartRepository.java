package vn.edu.iuh.fit.food.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.food.model.Cart;


public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomer_Id(Long userId);
}