package com.viniciusog.userjwtexample.payload;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

}
