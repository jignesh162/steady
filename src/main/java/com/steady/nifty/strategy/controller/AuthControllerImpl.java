package com.steady.nifty.strategy.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.steady.nifty.strategy.entity.Authority;
import com.steady.nifty.strategy.entity.Role;
import com.steady.nifty.strategy.entity.User;
import com.steady.nifty.strategy.payload.request.LoginRequest;
import com.steady.nifty.strategy.payload.request.SignupRequest;
import com.steady.nifty.strategy.payload.request.UserUpdateRequest;
import com.steady.nifty.strategy.payload.response.JwtResponse;
import com.steady.nifty.strategy.security.service.AuthService;

@RestController
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    @Autowired
    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @Override
    public User createUser(SignupRequest signupRequest) {
        return authService.createUser(signupRequest);
    }

    @Override
    public User updateUser(UserUpdateRequest updateRequest) {
        return authService.updateUser(updateRequest);
    }

    @Override
    public void deleteUser(String username) {
        authService.deleteUser(username);
    }

    @Override
    public Iterable<User> getUsers() {
        return authService.getUsers();
    }

    @Override
    public User getUser(String username) {
        return authService.getUser(username);
    }

    @Override
    public Iterable<Authority> getAuthorities() {
        return authService.getAuthorities();
    }

    @Override
    public Iterable<User> getUsersByAuthority(Role role) {
        return authService.getUsersByAuthority(role);
    }

    @Override
    public List<User> getRoleWiseUsersList() {
        List<User> roleWiseUsers = new ArrayList<User>();
        for (Role role : Role.values()) {
            Iterator<User> userIterator = getUsersByAuthority(role).iterator();
            while (userIterator.hasNext()) {
                roleWiseUsers.add(userIterator.next());
            }
        }
        return roleWiseUsers;
    }
}
