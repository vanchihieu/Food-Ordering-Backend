package vn.edu.iuh.fit.food.request;

import lombok.Data;
import vn.edu.iuh.fit.food.model.Address;

@Data
public class CreateOrderRequest {
    private Long restaurantId;
    private Address deliveryAddress;
}
