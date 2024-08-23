package vn.edu.iuh.fit.food.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import vn.edu.iuh.fit.food.domain.USER_ROLE;
import vn.edu.iuh.fit.food.dto.RestaurantDto;

import lombok.Data;

@Entity
@Table(name = "users1")
@AllArgsConstructor
@NoArgsConstructor
@Data // Lombok annotation to generate getter and setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;
    private String email;
    private String password;

    private USER_ROLE role;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    @ElementCollection
    private List<RestaurantDto> favorites = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    private String status;

}
