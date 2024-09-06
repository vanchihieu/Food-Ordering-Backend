package vn.edu.iuh.fit.food.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.food.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u Where u.status='PENDING'")
    public List<User> getPendingRestaurantOwners();

    public User findByEmail(String username);

}