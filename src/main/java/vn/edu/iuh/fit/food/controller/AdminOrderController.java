package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Order;
import vn.edu.iuh.fit.food.service.OrderService;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Order Controller", description = "Admin Order Controller")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Operation(method = "DELETE", summary = "Delete Order", description = "Delete Order")
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) throws InvalidDataException {
        if (orderId != null) {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Order deleted with id)" + orderId);
        } else return ResponseEntity.badRequest().body("Order id is null");
    }

    @Operation(method = "GET", summary = "Get All Orders", description = "Get All Orders")
    @GetMapping("/order/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getAllRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String order_status) throws InvalidDataException {
        List<Order> orders = orderService.getOrdersOfRestaurant(restaurantId, order_status);
        return ResponseEntity.ok(orders);
    }

    @Operation(method = "PUT", summary = "Update Orders", description = "Update Orders")
    @PutMapping("/orders/{orderId}/{orderStatus}")
    public ResponseEntity<Order> updateOrders(@PathVariable Long orderId, @PathVariable String orderStatus) throws InvalidDataException {
        Order orders = orderService.updateOrder(orderId, orderStatus);
        return ResponseEntity.ok(orders);
    }
}
