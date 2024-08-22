package vn.edu.iuh.fit.food.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import vn.edu.iuh.fit.food.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u Where u.status='PENDING'")
    public List<User> getPenddingRestaurantOwners();

    public User findByEmail(String username);

}