package vn.edu.iuh.fit.food.service;

import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Category;
import vn.edu.iuh.fit.food.model.Food;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) throws InvalidDataException;

    void deleteFood(Long foodId) throws InvalidDataException;

    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVegetarian, boolean isNonveg, boolean isSeasonal, String foodCategory) throws InvalidDataException;

    public List<Food> searchFood(String keyword);

    public Food findFoodById(Long foodId) throws InvalidDataException;

    public Food updateAvailibilityStatus(Long foodId) throws InvalidDataException;
}
