package vn.edu.iuh.fit.food.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.edu.iuh.fit.food.config.JwtProvider;
import vn.edu.iuh.fit.food.domain.USER_ROLE;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Cart;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.repository.CartRepository;
import vn.edu.iuh.fit.food.repository.UserRepository;
import vn.edu.iuh.fit.food.response.AuthResponse;
import vn.edu.iuh.fit.food.service.UserService;
import vn.edu.iuh.fit.food.service.impl.CustomerUserServiceImplementation;

import java.util.ArrayList;
import java.util.List;

public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomerUserServiceImplementation customUserDetails;
    private final CartRepository cartRepository;

//    private PasswordResetTokenService passwordResetTokenService;

    private final UserService userService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider,
                          CustomerUserServiceImplementation customUserDetails,
                          CartRepository cartRepository,
//                          PasswordResetTokenService passwordResetTokenService,
                          UserService userService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customUserDetails = customUserDetails;
        this.cartRepository = cartRepository;
//        this.passwordResetTokenService=passwordResetTokenService;
        this.userService = userService;

    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody User user) {

        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        USER_ROLE role = user.getRole();

        User isEmailExist = userRepository.findByEmail(email);

        if (isEmailExist != null) {
            throw new InvalidDataException("Email Is Already Used With Another Account");
        }

        // Create new user
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setRole(role);

        User savedUser = userRepository.save(createdUser);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        Cart savedCart = cartRepository.save(cart);
//		savedUser.setCart(savedCart);

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(role.toString()));

        // nạp input gồm email, password và authorities vào authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password, authorities);

        // set thông tin người dùng vào context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // tạo token
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setRole(savedUser.getRole());

        return ResponseEntity.ok().body(authResponse);

    }
}
