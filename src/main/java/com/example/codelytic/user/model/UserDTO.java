package com.example.codelytic.user.model;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    // private int gender;
    private String email;
    // private String birthDate;
    // private int subscriptionStatus;
    // <<<<<<< auth
    // private Role role;
    // =======
    // private Role role;
    // >>>>>>> master
    private String password;
    // private String username;
}
