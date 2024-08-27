package vn.edu.iuh.fit.food.service;

import vn.edu.iuh.fit.food.model.Notification;
import vn.edu.iuh.fit.food.model.Order;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.model.User;

import java.util.List;

public interface NotificationService {
    public Notification sendOrderStatusNotification(Order order);

    public void sendRestaurantNotification(Restaurant restaurant, String message);

    public void sendPromotionalNotification(User user, String message);

    public List<Notification> findUsersNotification(Long userId);
}
