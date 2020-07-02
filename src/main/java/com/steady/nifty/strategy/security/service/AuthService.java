package com.steady.nifty.strategy.security.service;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;

import com.steady.nifty.strategy.entity.Authority;
import com.steady.nifty.strategy.entity.Role;
import com.steady.nifty.strategy.entity.User;
import com.steady.nifty.strategy.payload.request.LoginRequest;
import com.steady.nifty.strategy.payload.request.SignupRequest;
import com.steady.nifty.strategy.payload.request.UserUpdateRequest;
import com.steady.nifty.strategy.payload.response.JwtResponse;
import com.steady.nifty.strategy.security.IsAdmin;

public interface AuthService {

    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

    @IsAdmin
    public User createUser(@Valid @RequestBody SignupRequest signupRequest);

    @IsAdmin
    @PreAuthorize("hasRole('ROLE_ADMIN') && #updateRequest.username != authentication.principal.username")
    public User updateUser(@Valid @RequestBody UserUpdateRequest updateRequest);

    @IsAdmin
    @PreAuthorize("hasRole('ROLE_ADMIN') && #username != authentication.principal.username")
    public void deleteUser(String username);

    @IsAdmin
    public Iterable<User> getUsers();

    @IsAdmin
    public User getUser(String username);

    @IsAdmin
    public Iterable<Authority> getAuthorities();

    @IsAdmin
    public Iterable<User> getUsersByAuthority(Role role);
}
