package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.food.model.IngredientCategory;
import vn.edu.iuh.fit.food.model.IngredientsItem;
import vn.edu.iuh.fit.food.request.CreateIngredientCategoryRequest;
import vn.edu.iuh.fit.food.request.CreateIngredientRequest;
import vn.edu.iuh.fit.food.service.IngredientsService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
@Tag(name = "Ingredients", description = "The Ingredients API")
public class IngredientsController {
    @Autowired
    private IngredientsService ingredientService;

    @Operation(method = "POST", summary = "Create Ingredient Category", description = "Create a new ingredient category")
    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(@RequestBody CreateIngredientCategoryRequest req) throws Exception {
        IngredientCategory items = ingredientService.createIngredientsCategory(req.getName(), req.getRestaurantId());
        return ResponseEntity.status(HttpStatus.CREATED).body(items);
    }

    @Operation(method = "POST", summary = "Create Ingredient Item", description = "Create a new ingredient item")
    @PostMapping()
    public ResponseEntity<IngredientsItem> createIngredientItem(@RequestBody CreateIngredientRequest req) throws Exception {
        IngredientsItem item = ingredientService.createIngredientsItem(req.getRestaurantId(), req.getName(), req.getIngredientCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @Operation(method = "PUT", summary = "Update Ingredient Item Stoke", description = "Update ingredient item stoke")
    @PutMapping("/{id}/stoke")
    public ResponseEntity<IngredientsItem> updateStoke(@PathVariable Long id) throws Exception {
        IngredientsItem item = ingredientService.updateStoke(id);
        return ResponseEntity.ok().body(item);
    }

    @Operation(method = "GET", summary = "Get Ingredient Item By Restaurant", description = "Get ingredient item by restaurant")
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredientsItem>> getIngredientItemByRestaurant(@PathVariable Long id) throws Exception {
        List<IngredientsItem> items = ingredientService.findIngredientsItemByRestaurantsId(id);
        return ResponseEntity.ok().body(items);
    }

    @Operation(method = "GET", summary = "Get Ingredient Category By Restaurant", description = "Get ingredient category by restaurant")
    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategory>> getIngredientCategoryByRestaurant(@PathVariable Long id) throws Exception {
        List<IngredientCategory> items = ingredientService.findIngredientsCategoryByRestaurantId(id);
        return ResponseEntity.ok().body(items);
    }
}
