package vn.edu.iuh.fit.food.service;

import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Category;

import java.util.List;

public interface CategoryService {
    public Category createCategory(String name, Long userId) throws InvalidDataException;

    public List<Category> findCategoryByRestaurantId(Long restaurantId) throws InvalidDataException;

    public Category findCategoryById(Long id) throws InvalidDataException;
}