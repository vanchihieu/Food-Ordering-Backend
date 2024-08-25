package vn.edu.iuh.fit.food.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Category;
import vn.edu.iuh.fit.food.model.Food;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.repository.FoodRepository;
import vn.edu.iuh.fit.food.request.CreateFoodRequest;
import vn.edu.iuh.fit.food.service.FoodService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImplementation implements FoodService {
    @Autowired
    private FoodRepository foodRepository;

//    @Autowired
//    private IngredientsService ingredientService;

//    @Autowired
//    private IngredientsCategoryRepository ingredientCategoryRepo;

    @Override
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) throws InvalidDataException {
        Food food = new Food();
        food.setFoodCategory(category);
        food.setCreationDate(new Date());
        food.setDescription(req.getDescription());
        food.setImages(req.getImages());
        food.setName(req.getName());
        food.setPrice((long) req.getPrice());
        food.setSeasonal(req.isSeasonal());
        food.setVegetarian(req.isVegetarian());
        food.setIngredients(req.getIngredients());
        food.setRestaurant(restaurant);
        food = foodRepository.save(food);

        restaurant.getFoods().add(food);
        return food;
    }

    @Override
    public void deleteFood(Long foodId) throws InvalidDataException {
        Food food = findFoodById(foodId);
        food.setRestaurant(null);
//		foodRepository.save(food);
        foodRepository.delete(food);
    }

    @Override
    public List<Food> getRestaurantsFood(
            Long restaurantId,
            boolean isVegetarian,
            boolean isNonveg,
            boolean isSeasonal,
            String foodCategory) throws InvalidDataException {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);

        if (isVegetarian) {
            foods = filterByVegetarian(foods, isVegetarian);
        }
        if (isNonveg) {
            foods = filterByNonveg(foods, isNonveg);
        }

        if (isSeasonal) {
            foods = filterBySeasonal(foods, isSeasonal);
        }
        if (foodCategory != null && !foodCategory.equals("")) {
            foods = filterByFoodCategory(foods, foodCategory);
        }

        return foods;
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
        return foods.stream()
                .filter(food -> food.isVegetarian() == isVegetarian)
                .collect(Collectors.toList());
    }

    private List<Food> filterByNonveg(List<Food> foods, boolean isNonveg) {
        return foods.stream()
                .filter(food -> food.isVegetarian() == false)
                .collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
        return foods.stream()
                .filter(food -> food.isSeasonal() == isSeasonal)
                .collect(Collectors.toList());
    }

    private List<Food> filterByFoodCategory(List<Food> foods, String foodCategory) {
        return foods.stream()
                .filter(food -> {
                    if (food.getFoodCategory() != null) {
                        return food.getFoodCategory().getName().equals(foodCategory);
                    }
                    return false; // Return true if food category is null
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        List<Food> items = new ArrayList<>();

        if (keyword != "") {
            System.out.println("keyword -- " + keyword);
            items = foodRepository.searchByNameOrCategory(keyword);
        }

        return items;
    }

    @Override
    public Food updateAvailibilityStatus(Long id) throws InvalidDataException {
        Food food = findFoodById(id);

        food.setAvailable(!food.isAvailable());
        foodRepository.save(food);
        return food;
    }

    @Override
    public Food findFoodById(Long foodId) throws InvalidDataException {
        Optional<Food> food = foodRepository.findById(foodId);
        if (food.isPresent()) {
            return food.get();
        }
        throw new InvalidDataException("food with id" + foodId + "not found");
    }
}