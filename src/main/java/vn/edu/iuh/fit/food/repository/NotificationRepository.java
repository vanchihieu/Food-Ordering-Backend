package vn.edu.iuh.fit.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.food.model.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findByCustomerId(Long userId);

    public List<Notification> findByRestaurantId(Long restaurantId);
}