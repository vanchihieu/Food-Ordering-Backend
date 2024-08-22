package vn.edu.iuh.fit.food.response;

import lombok.Data;
import vn.edu.iuh.fit.food.domain.USER_ROLE;

@Data
public class AuthResponse {
    private String message;
    private String jwt;
    private USER_ROLE role;
}