package com.example.codelytic.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        user.setGender(userDTO.getGender());
        user.setEmail(userDTO.getEmail());
        user.setBirthDate(userDTO.getBirthDate());
        user.setSubscriptionStatus(userDTO.getSubscriptionStatus());
        user.setRole(userDTO.getRole());

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        try {
            user = userService.saveUser(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Map<String, Long> response = new HashMap<>();
        response.put("id", user.getId());
        return ResponseEntity.ok(response);
    }
}