package vn.edu.iuh.fit.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.iuh.fit.food.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
}