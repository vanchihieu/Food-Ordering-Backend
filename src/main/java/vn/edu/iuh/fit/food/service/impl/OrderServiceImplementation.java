package vn.edu.iuh.fit.food.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.*;
import vn.edu.iuh.fit.food.repository.*;
import vn.edu.iuh.fit.food.request.CreateOrderRequest;
import vn.edu.iuh.fit.food.response.PaymentResponse;
import vn.edu.iuh.fit.food.service.CartService;
import vn.edu.iuh.fit.food.service.NotificationService;
import vn.edu.iuh.fit.food.service.OrderService;
import vn.edu.iuh.fit.food.service.PaymentService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImplementation implements OrderService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public PaymentResponse createOrder(CreateOrderRequest order, User user) throws InvalidDataException {
        // Lưu địa chỉ giao hàng
        Address shippAddress = order.getDeliveryAddress();
        Address savedAddress = addressRepository.save(shippAddress);

        // kiểm tra xem user đã có address chưa nếu chưa thì thêm vào
        if (!user.getAddresses().contains(savedAddress)) {
            user.getAddresses().add(savedAddress);
        }

        log.info("User Addresses: " + user.getAddresses());

        userRepository.save(user);

        // kiểm tra xem restaurant có tồn tại không
        Optional<Restaurant> restaurant = restaurantRepository.findById(order.getRestaurantId());
        if (restaurant.isEmpty()) {
            throw new InvalidDataException("Restaurant not found with id " + order.getRestaurantId());
        }

        // Tạo order
        Order createdOrder = new Order();
        createdOrder.setCustomer(user);
        createdOrder.setDeliveryAddress(savedAddress);
        createdOrder.setCreatedAt(new Date());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.setRestaurant(restaurant.get());

        // tìm kiếm giỏ hàng của user
        Cart cart = cartService.findCartByUserId(user.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        /**
         * khi tạo đơn hàng, thông tin về các mục trong giỏ hàng được sao chép chính xác vào đơn hàng,
         * giúp đảm bảo tính chính xác và thống nhất của dữ liệu trong hệ thống.
         */
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getFood().getPrice() * cartItem.getQuantity());

            // lưu thông tin món ăn vào database
            OrderItem savedOrderItem = orderItemRepository.save(orderItem);

            orderItems.add(savedOrderItem);
        }

        // Tính tổng tiền
        Long totalPrice = cartService.calculateCartTotals(cart);

        createdOrder.setTotalAmount(totalPrice);
        createdOrder.setRestaurant(restaurant.get());
        createdOrder.setItems(orderItems);

        Order savedOrder = orderRepository.save(createdOrder);

        // lấy danh sách các đơn hàng của nhà hàng và thêm đơn hàng mới vào
        restaurant.get().getOrders().add(savedOrder);

        // lưu lại thông tin của nhà hàng
        restaurantRepository.save(restaurant.get());

        PaymentResponse res = paymentService.generatePaymentLink(savedOrder);
        return res;
    }

    @Override
    public void cancelOrder(Long orderId) throws InvalidDataException {
        Order order = findOrderById(orderId);
        if (order == null) {
            throw new InvalidDataException("Order not found with the id " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    public Order findOrderById(Long orderId) throws InvalidDataException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) return order.get();
        throw new InvalidDataException("Order not found with the id " + orderId);
    }

    @Override
    public List<Order> getUserOrders(Long userId) throws InvalidDataException {
        List<Order> orders = orderRepository.findAllUserOrders(userId);
        return orders;
    }

    @Override
    public List<Order> getOrdersOfRestaurant(Long restaurantId, String orderStatus) throws InvalidDataException {
        List<Order> orders = orderRepository.findOrdersByRestaurantId(restaurantId);

        if (orderStatus != null) {
            orders = orders.stream()
                    .filter(order -> order.getOrderStatus().equals(orderStatus))
                    .collect(Collectors.toList());
        }
        return orders;
    }
//    private List<MenuItem> filterByVegetarian(List<MenuItem> menuItems, boolean isVegetarian) {
//    return menuItems.stream()
//            .filter(menuItem -> menuItem.isVegetarian() == isVegetarian)
//            .collect(Collectors.toList());
//}

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws InvalidDataException {
        Order order = findOrderById(orderId);

        log.info("Order Status: " + orderStatus);

        if (orderStatus.equals("OUT_FOR_DELIVERY") || orderStatus.equals("DELIVERED")
                || orderStatus.equals("COMPLETED") || orderStatus.equals("PENDING")) {
            order.setOrderStatus(orderStatus);
            Notification notification = notificationService.sendOrderStatusNotification(order);
            return orderRepository.save(order);
        } else throw new InvalidDataException("Please Select A Valid Order Status");
    }
}
