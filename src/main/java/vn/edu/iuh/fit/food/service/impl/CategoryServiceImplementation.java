package vn.edu.iuh.fit.food.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Category;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.repository.CategoryRepository;
import vn.edu.iuh.fit.food.service.CategoryService;
import vn.edu.iuh.fit.food.service.RestaurantService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(String name, Long userId) throws InvalidDataException {
        Restaurant restaurant = restaurantService.getRestaurantsByUserId(userId);
        Category createdCategory = new Category();

        createdCategory.setName(name);
        createdCategory.setRestaurant(restaurant);
        return categoryRepository.save(createdCategory);
    }

    /**
     * tìm danh sách category theo id của restaurant
     *
     * @param id
     * @return
     * @throws InvalidDataException
     */
    @Override
    public List<Category> findCategoryByRestaurantId(Long id) throws InvalidDataException {
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        return categoryRepository.findByRestaurantId(id);
    }

    @Override
    public Category findCategoryById(Long id) throws InvalidDataException {
        Optional<Category> opt = categoryRepository.findById(id);
        if (opt.isEmpty()) {
            throw new InvalidDataException("category not exist with id " + id);
        }
        return opt.get();
    }
}
