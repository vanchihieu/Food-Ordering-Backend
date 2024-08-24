package vn.edu.iuh.fit.food.service;

import vn.edu.iuh.fit.food.dto.RestaurantDto;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user);

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant)
            throws InvalidDataException;

    public void deleteRestaurant(Long restaurantId) throws InvalidDataException;

    public List<Restaurant> getAllRestaurant();

    public List<Restaurant> searchRestaurant(String keyword);

    public Restaurant findRestaurantById(Long id) throws InvalidDataException;

    public Restaurant getRestaurantsByUserId(Long userId) throws InvalidDataException;

    public RestaurantDto addToFavorites(Long restaurantId, User user) throws InvalidDataException;

    public Restaurant updateRestaurantStatus(Long id) throws InvalidDataException;
}
