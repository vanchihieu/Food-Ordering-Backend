package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Food;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.request.CreateFoodRequest;
import vn.edu.iuh.fit.food.service.FoodService;
import vn.edu.iuh.fit.food.service.RestaurantService;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/food")
@Slf4j
@Tag(name = "Admin Food Controller", description = "Admin Food Controller")
public class AdminFoodController {
    @Autowired
    private FoodService foodService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;

//    @Autowired
//    private CategoryService categoryService;

    @Operation(method = "Post", summary = "Create a new food item", description = "Create a new food item")
    @PostMapping()
    public ResponseEntity<Food> createFood(
            @RequestBody CreateFoodRequest item,
            @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        log.info("Creating new food item");
        User user = userService.findUserProfileByJwt(jwt);
//		Category category=categoryService.findCategoryById(item.getCategoryId());
        Restaurant restaurant = restaurantService.findRestaurantById(item.getRestaurantId());
        Food menuItem = foodService.createFood(item, item.getCategory(), restaurant);
        return ResponseEntity.ok(menuItem);
    }

    @Operation(method = "Get", summary = "Get all food items", description = "Get all food items")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);
        foodService.deleteFood(id);
        return ResponseEntity.ok("Menu item deleted");
    }

    @Operation(method = "Get", summary = "Search for a food item by name using a keyword", description = "Search for a food item by name using a keyword")
    @GetMapping("/search")
    public ResponseEntity<List<Food>> getFoodByName(@RequestParam String name) {
        List<Food> menuItem = foodService.searchFood(name);
        return ResponseEntity.ok(menuItem);
    }

    @Operation(method = "Put", summary = "Update the availability status of a food item", description = "Update the availability status of a food item")
    @PutMapping("/{id}")
    public ResponseEntity<Food> updateAvilibilityStatus(@PathVariable Long id) throws InvalidDataException {
        Food menuItems = foodService.updateAvailibilityStatus(id);
        return ResponseEntity.ok(menuItems);
    }
}
