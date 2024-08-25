package vn.edu.iuh.fit.food.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.dto.RestaurantDto;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Address;
import vn.edu.iuh.fit.food.model.Restaurant;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.repository.AddressRepository;
import vn.edu.iuh.fit.food.repository.RestaurantRepository;
import vn.edu.iuh.fit.food.repository.UserRepository;
import vn.edu.iuh.fit.food.request.CreateRestaurantRequest;
import vn.edu.iuh.fit.food.service.RestaurantService;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImplementation implements RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {
        Address address = new Address();
        address.setCity(req.getAddress().getCity());
        address.setCountry(req.getAddress().getCountry());
        address.setFullName(req.getAddress().getFullName());
        address.setPostalCode(req.getAddress().getPostalCode());
        address.setState(req.getAddress().getState());
        address.setStreetAddress(req.getAddress().getStreetAddress());
        Address savedAddress = addressRepository.save(address);

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(savedAddress);
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setDescription(req.getDescription());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(req.getRegistrationDate());
        restaurant.setOwner(user);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return savedRestaurant;
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedReq)
            throws InvalidDataException {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if (restaurant.getCuisineType() != null) {
            restaurant.setCuisineType(updatedReq.getCuisineType());
        }
        if (restaurant.getDescription() != null) {
            restaurant.setDescription(updatedReq.getDescription());
        }
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant findRestaurantById(Long restaurantId) throws InvalidDataException {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant.isPresent()) {
            return restaurant.get();
        } else {
            throw new InvalidDataException("Restaurant with id " + restaurantId + "not found");
        }
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws InvalidDataException {
        Restaurant restaurant = findRestaurantById(restaurantId);
        if (restaurant != null) {
            restaurantRepository.delete(restaurant);
            return;
        }
        throw new InvalidDataException("Restaurant with id " + restaurantId + " Not found");

    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }


    @Override
    public Restaurant getRestaurantsByUserId(Long userId) throws InvalidDataException {
        Restaurant restaurants = restaurantRepository.findByOwnerId(userId);
        return restaurants;
    }


    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public RestaurantDto addToFavorites(Long restaurantId, User user) throws InvalidDataException {
        Restaurant restaurant = findRestaurantById(restaurantId);

        RestaurantDto dto = new RestaurantDto();
        dto.setTitle(restaurant.getName());
        dto.setImages(restaurant.getImages());
        dto.setId(restaurant.getId());
        dto.setDescription(restaurant.getDescription());

        /**
         *  kiểm tra xem nhà hàng đã được yêu thích bởi người dùng chưa bằng cách duyệt qua danh sách các nhà hàng yêu thích của người dùng (favorites). Nếu tìm thấy nhà hàng trong danh sách yêu thích, nó đặt isFavorited thành true.
         */
        boolean isFavorited = false;
        List<RestaurantDto> favorites = user.getFavorites();
        for (RestaurantDto favorite : favorites) {
            if (favorite.getId().equals(restaurantId)) {
                isFavorited = true;
                break;
            }
        }

        /**
         * Nếu nhà hàng đã được yêu thích, nó sẽ xóa khỏi danh sách yêu thích của người dùng. Nếu không, nó sẽ thêm vào danh sách yêu thích của người dùng.
         */
        if (isFavorited) {
            favorites.removeIf(favorite -> favorite.getId().equals(restaurantId));
        } else {
            favorites.add(dto);
        }

        User updatedUser = userRepository.save(user);
        return dto;
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws InvalidDataException {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
}
