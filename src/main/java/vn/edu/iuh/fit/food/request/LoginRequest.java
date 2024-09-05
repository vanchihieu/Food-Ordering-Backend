package vn.edu.iuh.fit.food.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}