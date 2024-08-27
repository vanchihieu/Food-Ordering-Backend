package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Order;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.request.CreateOrderRequest;
import vn.edu.iuh.fit.food.response.PaymentResponse;
import vn.edu.iuh.fit.food.service.OrderService;
import vn.edu.iuh.fit.food.service.PaymentService;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "Order Controller", description = "Order Controller")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;

    @Operation(method = "POST", summary = "Create Order", description = "Create Order")
    @PostMapping("/order")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody CreateOrderRequest order,
                                                       @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);
        System.out.println("req user " + user.getEmail());
        if (order != null) {
            PaymentResponse res = orderService.createOrder(order, user);
            return ResponseEntity.ok(res);

        } else throw new InvalidDataException("Please provide valid request body");
    }

    @Operation(method = "GET", summary = "Get All User Orders", description = "Get All User Orders")
    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getAllUserOrders(@RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);

        if (user.getId() != null) {
            List<Order> userOrders = orderService.getUserOrders(user.getId());
            return ResponseEntity.ok(userOrders);
        } else {
            return new ResponseEntity<List<Order>>(HttpStatus.BAD_REQUEST);
        }
    }
}
