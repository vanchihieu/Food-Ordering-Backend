package vn.edu.iuh.fit.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.food.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findByRestaurantId(Long id);
}
