package vn.edu.iuh.fit.food.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.model.Notification;
import vn.edu.iuh.fit.food.model.Order;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.repository.NotificationRepository;
import vn.edu.iuh.fit.food.service.NotificationService;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImplementation implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification sendOrderStatusNotification(Order order) {
        Notification notification = new Notification();
        notification.setMessage("your order is " + order.getOrderStatus() + " order id is - " + order.getId());
        notification.setCustomer(order.getCustomer());
        notification.setSentAt(new Date());

        return notificationRepository.save(notification);
    }

    @Override
    public void sendRestaurantNotification(Restaurant restaurant, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPromotionalNotification(User user, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Notification> findUsersNotification(Long userId) {
        // TODO Auto-generated method stub
        return notificationRepository.findByCustomerId(userId);
    }
}
