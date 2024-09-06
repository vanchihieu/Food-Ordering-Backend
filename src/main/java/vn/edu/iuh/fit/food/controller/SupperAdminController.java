package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.List;

@RestController
@Tag(name = "Supper Admin Controller", description = "Supper Admin Controller")
public class SupperAdminController {
    @Autowired
    private UserService userService;

    @Operation(method = "GET", summary = "Get All Customers", description = "Get All Customers")
    @GetMapping("/api/customers")
    public ResponseEntity<List<User>> getAllCustomers() {
        List<User> users = userService.findAllUsers();

        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }

    @Operation(method = "GET", summary = "Get All Pending Restaurant Owners", description = "Get All Pending Restaurant Owners")
    @GetMapping("/api/pending-customers")
    public ResponseEntity<List<User>> getPendingRestaurantUser() {
        List<User> users = userService.getPendingRestaurantOwner();
        return new ResponseEntity<List<User>>(users, HttpStatus.ACCEPTED);
    }
}
