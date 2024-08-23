package vn.edu.iuh.fit.food.service;

import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.User;

public interface UserService {
    public User findUserProfileByJwt(String jwt) throws InvalidDataException;

    public User findUserByEmail(String email) throws InvalidDataException;
}
