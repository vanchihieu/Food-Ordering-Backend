package vn.edu.iuh.fit.food.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.config.JwtProvider;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.PasswordResetToken;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.repository.PasswordResetTokenRepository;
import vn.edu.iuh.fit.food.repository.UserRepository;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private JavaMailSender javaMailSender;

    public UserServiceImplementation(
            UserRepository userRepository,
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder
//            PasswordResetTokenRepository passwordResetTokenRepository,
//            JavaMailSender javaMailSender
    ) {

        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
//        this.passwordResetTokenRepository=passwordResetTokenRepository;
//        this.javaMailSender=javaMailSender;

    }

    @Override
    public User findUserProfileByJwt(String jwt) throws InvalidDataException {
        String email = jwtProvider.getEmailFromJwtToken(jwt);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new InvalidDataException("user not exist with email " + email);
        }
//		System.out.println("email user "+user.get().getEmail());
        return user;
    }

    @Override
    public User findUserByEmail(String username) throws InvalidDataException {
        User user = userRepository.findByEmail(username);

        if (user != null) {
            return user;
        }

        throw new InvalidDataException("user not exist with username " + username);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getPendingRestaurantOwner() {
        return userRepository.getPendingRestaurantOwners();
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void sendPasswordResetEmail(User user) {
        String resetToken = generateRandomToken();

        // Calculate expiry date
        Date expiryDate = calculateExpiryDate();

        // Save the token in the database
        PasswordResetToken passwordResetToken = new PasswordResetToken(resetToken, user, expiryDate);
        passwordResetTokenRepository.save(passwordResetToken);

        // Send an email containing the reset link
        sendEmail(user.getEmail(), "Password Reset", "Click the following link to reset your password: http://localhost:3000/account/reset-password?token=" + resetToken);
    }

    private void sendEmail(String to, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 10);
        return cal.getTime();
    }
}
