package com.example.codelytic.auth;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthenticationRequestDto implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;

}