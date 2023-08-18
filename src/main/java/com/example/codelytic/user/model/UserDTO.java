package com.example.codelytic.user.model;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private int gender;
    private String email;
    private String birthDate;
    private int subscriptionStatus;
    private Role role;
    private String password;
    // private String username;
}