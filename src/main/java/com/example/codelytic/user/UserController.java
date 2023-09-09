package com.example.codelytic.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codelytic.progress.model.Progress;
import com.example.codelytic.user.model.CreateUserDTO;
import com.example.codelytic.user.model.Role;
import com.example.codelytic.user.model.User;

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
    ResponseEntity<?> createUser(@RequestBody CreateUserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        // user.setSubscriptionStatus(userDTO.getSubscriptionStatus());
        user.setRole(userDTO.getRole());

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        try {
            user.setProgress(new Progress());
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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUser(email);
        // GetUserDTO result = new GetUserDTO();
        //
        // result.setName(user.getName());
        // result.setEmail(user.getEmail());
        // result.setRole(user.getRole());
        // result.setId(user.getId());

        return ResponseEntity.ok(user);
    }

    /*
     * Get all user
     * will be called when the authority is admin
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/role/{userEmail}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> changeRole(
            @PathVariable String userEmail,
            @RequestBody Role role) {

        User user = userService.getUser(userEmail);
        user.setRole(role);
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }
}