package com.example.codelytic.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.codelytic.user.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(String email) {
        return userRepository
                .findByEmail(email)
                .orElse(null);
    }

    public Object getAllUser() {
        return userRepository.findAll();
    }
}
