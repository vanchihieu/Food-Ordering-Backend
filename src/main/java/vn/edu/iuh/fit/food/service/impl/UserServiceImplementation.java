package vn.edu.iuh.fit.food.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.config.JwtProvider;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.repository.UserRepository;
import vn.edu.iuh.fit.food.service.UserService;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
//    private PasswordResetTokenRepository passwordResetTokenRepository;
//    private JavaMailSender javaMailSender;

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
}
