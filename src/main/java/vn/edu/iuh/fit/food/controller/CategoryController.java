package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Category;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.service.CategoryService;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "Category Controller", description = "Category Controller")
public class CategoryController {
    @Autowired
    public CategoryService categoryService;

    @Autowired
    public UserService userService;

    @Operation(method = "Post", summary = "Create a new category", description = "Create a new category")
    @PostMapping("/admin/category")
    public ResponseEntity<Category> createdCategory(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Category category) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);
        Category createdCategory = categoryService.createCategory(category.getName(), user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @Operation(method = "Get", summary = "Get all categories", description = "Get all categories")
    @GetMapping("/category/restaurant/{id}")
    public ResponseEntity<List<Category>> getCategoryByRestaurant(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Category> categories = categoryService.findCategoryByRestaurantId(id);
        return ResponseEntity.ok().body(categories);
    }
}
