package vn.edu.iuh.fit.food.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.IngredientCategory;
import vn.edu.iuh.fit.food.model.IngredientsItem;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.repository.IngredientsCategoryRepository;
import vn.edu.iuh.fit.food.repository.IngredientsItemRepository;
import vn.edu.iuh.fit.food.service.IngredientsService;
import vn.edu.iuh.fit.food.service.RestaurantService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class IngredientsServiceImplementation implements IngredientsService {
    @Autowired
    private IngredientsCategoryRepository ingredientsCategoryRepo;

    @Autowired
    private IngredientsItemRepository ingredientsItemRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public IngredientCategory createIngredientsCategory(String name, Long restaurantId) throws InvalidDataException {
        IngredientCategory isExist = ingredientsCategoryRepo.findByRestaurantIdAndNameIgnoreCase(restaurantId, name);

        if (isExist != null) {
            return isExist;
        }

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setRestaurant(restaurant);
        ingredientCategory.setName(name);

        IngredientCategory createdCategory = ingredientsCategoryRepo.save(ingredientCategory);

        return createdCategory;
    }

    @Override
    public IngredientCategory findIngredientsCategoryById(Long id) throws Exception {
        Optional<IngredientCategory> opt = ingredientsCategoryRepo.findById(id);
        if (opt.isEmpty()) {
            throw new Exception("ingredient category not found");
        }
        return opt.get();
    }

    @Override
    public List<IngredientCategory> findIngredientsCategoryByRestaurantId(Long id) throws Exception {
        return ingredientsCategoryRepo.findByRestaurantId(id);
    }

    @Override
    public List<IngredientsItem> findIngredientsItemByRestaurantsId(Long restaurantId) {
        return ingredientsItemRepository.findByRestaurantId(restaurantId);
    }

    /**
     * create ingredient item
     *
     * @param restaurantId         // id of restaurant
     * @param ingredientName       // name of ingredient
     * @param ingredientCategoryId // id of ingredient category
     * @return
     * @throws Exception
     */
    @Override
    public IngredientsItem createIngredientsItem(Long restaurantId,
                                                 String ingredientName, Long ingredientCategoryId) throws Exception {
        IngredientCategory category = findIngredientsCategoryById(ingredientCategoryId);

        IngredientsItem isExist = ingredientsItemRepository.
                findByRestaurantIdAndNameIngoreCase(restaurantId, ingredientName, category.getName());
        if (isExist != null) {
            log.info("ingredient is exist");
            return isExist;
        }

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientsItem item = new IngredientsItem();
        item.setName(ingredientName);
        item.setRestaurant(restaurant);
        item.setCategory(category);

        IngredientsItem savedIngredients = ingredientsItemRepository.save(item);
        category.getIngredients().add(savedIngredients);

        return savedIngredients;
    }


    @Override
    public IngredientsItem updateStoke(Long id) throws Exception {
        Optional<IngredientsItem> item = ingredientsItemRepository.findById(id);
        if (item.isEmpty()) {
            throw new Exception("ingredient not found with id " + item);
        }
        IngredientsItem ingredient = item.get();
        ingredient.setInStoke(!ingredient.isInStoke());
        return ingredientsItemRepository.save(ingredient);
    }
}
