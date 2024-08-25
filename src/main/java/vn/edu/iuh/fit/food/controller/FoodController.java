package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Food;
import vn.edu.iuh.fit.food.service.FoodService;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@Tag(name = "Food Controller", description = "Food Controller")
@Slf4j
public class FoodController {
    @Autowired
    private FoodService menuItemService;

    @Autowired
    private UserService userService;

    @Operation(method = "Get", summary = "Search for a food item by name using a keyword", description = "Search for a food item by name using a keyword")
    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(
            @RequestParam String name) {
        List<Food> menuItem = menuItemService.searchFood(name);
        return ResponseEntity.ok(menuItem);
    }

    @Operation(method = "Get", summary = "Get all food items", description = "Get all food items")
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getFoodByRestaurantId(
            @PathVariable Long restaurantId,
            @RequestParam boolean vegetarian,
            @RequestParam boolean seasonal,
            @RequestParam boolean nonveg,
            @RequestParam(required = false) String food_category) throws InvalidDataException {
        List<Food> menuItems = menuItemService.getRestaurantsFood(
                restaurantId, vegetarian, nonveg, seasonal, food_category);
        return ResponseEntity.ok(menuItems);
    }
}
