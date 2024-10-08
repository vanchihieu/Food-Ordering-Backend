package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.request.CreateRestaurantRequest;
import vn.edu.iuh.fit.food.response.ApiResponse;
import vn.edu.iuh.fit.food.service.RestaurantService;
import vn.edu.iuh.fit.food.service.UserService;


@RestController
@RequestMapping("/api/admin/restaurants")
@Tag(name = "Admin Restaurant Controller", description = "Admin Restaurant Controller")
@Slf4j
public class AdminRestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @Operation(method = "Post", summary = "Create a new restaurant", description = "Create a new restaurant")
    @PostMapping()
    public ResponseEntity<Restaurant> createRestaurant(
            @RequestBody CreateRestaurantRequest req,
            @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);
        log.info("Create Restaurant" + user);

        Restaurant restaurant = restaurantService.createRestaurant(req, user);
        return ResponseEntity.ok(restaurant);
    }

    @Operation(method = "Put", summary = "Update a restaurant", description = "Update a restaurant")
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
                                                       @RequestBody CreateRestaurantRequest req,
                                                       @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);
        log.info("Update Restaurant" + user);

        Restaurant restaurant = restaurantService.updateRestaurant(id, req);
        return ResponseEntity.ok(restaurant);
    }

    @Operation(method = "Delete", summary = "Delete a restaurant by ID", description = "Delete a restaurant by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRestaurantById(@PathVariable("id") Long restaurantId,
                                                            @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);

        restaurantService.deleteRestaurant(restaurantId);

        ApiResponse res = new ApiResponse("Restaurant Deleted with id Successfully", true);
        return ResponseEntity.ok(res);
    }

    @Operation(method = "Put", summary = "Update restaurant status", description = "Update restaurant status")
    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateStatusRestaurant(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id) throws InvalidDataException {
        Restaurant restaurant = restaurantService.updateRestaurantStatus(id);
        return ResponseEntity.ok(restaurant);
    }

    @Operation(method = "Get", summary = "Get restaurant by user ID", description = "Get restaurant by user ID")
    @GetMapping("/user")
    public ResponseEntity<Restaurant> findRestaurantByUserId(
            @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);
        Restaurant restaurant = restaurantService.getRestaurantsByUserId(user.getId());
        return ResponseEntity.ok(restaurant);
    }
}