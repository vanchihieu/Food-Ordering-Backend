package vn.edu.iuh.fit.food.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.food.exception.InvalidDataException;
import vn.edu.iuh.fit.food.model.Notification;
import vn.edu.iuh.fit.food.model.User;
import vn.edu.iuh.fit.food.service.NotificationService;
import vn.edu.iuh.fit.food.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> findUsersNotification(
            @RequestHeader("Authorization") String jwt) throws InvalidDataException {
        User user = userService.findUserProfileByJwt(jwt);

        List<Notification> notifications = notificationService.findUsersNotification(user.getId());
        return new ResponseEntity<List<Notification>>(notifications, HttpStatus.ACCEPTED);
    }
}
