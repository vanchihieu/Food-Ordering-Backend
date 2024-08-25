package vn.edu.iuh.fit.food.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.fit.food.model.Category;
import vn.edu.iuh.fit.food.model.IngredientsItem;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFoodRequest {
    private String name;
    private String description;
    private Long price;
    private Category category;
    private List<String> images;
    private Long restaurantId;
    private boolean vegetarian;
    private boolean seasonal;
    private List<IngredientsItem> ingredients;
}
