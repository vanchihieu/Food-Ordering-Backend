package vn.edu.iuh.fit.food.service;

import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.IngredientCategory;
import vn.edu.iuh.fit.food.model.IngredientsItem;

import java.util.List;

public interface IngredientsService {
    public IngredientCategory createIngredientsCategory(String name, Long restaurantId) throws InvalidDataException;

    public IngredientCategory findIngredientsCategoryById(Long id) throws Exception;

    public List<IngredientCategory> findIngredientsCategoryByRestaurantId(Long id) throws Exception;

    public List<IngredientsItem> findIngredientsItemByRestaurantsId(Long restaurantId);

    public IngredientsItem createIngredientsItem(Long restaurantId, String ingredientName, Long ingredientCategoryId) throws Exception;

    public IngredientsItem updateStoke(Long id) throws Exception;
}