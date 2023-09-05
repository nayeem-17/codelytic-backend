package com.example.codelytic.user.model;

import lombok.Data;

@Data

public class GetUserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
