package com.example.codelytic.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codelytic.user.model.Role;
import com.example.codelytic.user.model.User;
import com.example.codelytic.user.model.UserDTO;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /*
     * create a user
     */
    @PostMapping("/register")
    ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        // user.setSubscriptionStatus(userDTO.getSubscriptionStatus());
        user.setRole(Role.CONTENT_CREATOR);

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        try {
            user = userService.saveUser(user);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Map<String, Long> response = new HashMap<>();
        response.put("id", user.getId());
        return ResponseEntity.ok(response);
    }
    /*
     * get a user
     */
    @GetMapping("/")
    ResponseEntity<User> getUser() {
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(email);
        return ResponseEntity.ok(user);
    }
}