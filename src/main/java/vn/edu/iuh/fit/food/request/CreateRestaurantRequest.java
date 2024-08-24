package vn.edu.iuh.fit.food.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import vn.edu.iuh.fit.food.model.Address;
import vn.edu.iuh.fit.food.model.ContactInformation;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantRequest {
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<String> images;
    private LocalDateTime registrationDate;
}