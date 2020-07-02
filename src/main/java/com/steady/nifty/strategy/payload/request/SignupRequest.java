package com.steady.nifty.strategy.payload.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.steady.nifty.strategy.entity.Role;
import com.steady.nifty.strategy.security.ValidPassword;

import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    private String username;
    @NotBlank
    @ValidPassword
    private String password;
    private boolean enabled;
    private Set<Role> roles;
}
