package vn.edu.iuh.fit.food.service;

import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Order;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.request.CreateOrderRequest;
import vn.edu.iuh.fit.food.response.PaymentResponse;

import java.util.List;

public interface OrderService {
    public PaymentResponse createOrder(CreateOrderRequest order, User user) throws InvalidDataException;

    public Order updateOrder(Long orderId, String orderStatus) throws InvalidDataException;

    public void cancelOrder(Long orderId) throws InvalidDataException;

    public List<Order> getUserOrders(Long userId) throws InvalidDataException;

    public List<Order> getOrdersOfRestaurant(Long restaurantId, String orderStatus) throws InvalidDataException;
}
