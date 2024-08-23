package vn.edu.iuh.fit.food.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.food.model.Cart;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomer_Id(Long userId);
}