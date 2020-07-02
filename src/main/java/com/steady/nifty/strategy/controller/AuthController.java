package com.steady.nifty.strategy.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.steady.nifty.strategy.entity.Authority;
import com.steady.nifty.strategy.entity.Role;
import com.steady.nifty.strategy.entity.User;
import com.steady.nifty.strategy.payload.request.LoginRequest;
import com.steady.nifty.strategy.payload.request.SignupRequest;
import com.steady.nifty.strategy.payload.request.UserUpdateRequest;
import com.steady.nifty.strategy.payload.response.JwtResponse;
import com.steady.nifty.strategy.security.IsAdmin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@Tag(name = "authentication", description = "the authentication API")
@RequestMapping("/auth")
public interface AuthController {
    @Operation(summary = "Get authentication token", description = "Get authentication token for username and password. Ie. login.", tags = {
            "authentication" })
    @PostMapping("/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

    @Operation(summary = "Creates a new user", description = "Creates a new user", security = {
            @SecurityRequirement(name = "bearerToken") }, tags = { "authentication" })
    @IsAdmin
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@Valid @RequestBody SignupRequest signupRequest);

    @Operation(summary = "Updates the user", description = "Updates the user and roles. Allowed for admins but not on themselves.", security = {
            @SecurityRequirement(name = "bearerToken") }, tags = { "authentication" })
    @IsAdmin
    @PreAuthorize("hasRole('ROLE_ADMIN') && #updateRequest.username != authentication.principal.username")
    @PutMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@Valid @RequestBody UserUpdateRequest updateRequest);

    @Operation(summary = "Deletes the user", description = "Deletes the user. Allowed for admins but not on themselves.", security = {
            @SecurityRequirement(name = "bearerToken") }, tags = { "authentication" })
    @IsAdmin
    @PreAuthorize("hasRole('ROLE_ADMIN') && #username != authentication.principal.username")
    @DeleteMapping(value = "/users/{username}")
    public void deleteUser(
            @Parameter(description = "username of user to delete", required = true) @PathVariable String username);

    @Operation(summary = "Gets all users and their roles", description = "Gats all users and their roles.", security = {
            @SecurityRequirement(name = "bearerToken") }, tags = { "authentication" })
    @IsAdmin
    @GetMapping(value = "/users")
    public Iterable<User> getUsers();

    @Operation(summary = "Gets single user including roles", description = "Gets single user including roles.", security = {
            @SecurityRequirement(name = "bearerToken") }, tags = { "authentication" })
    @IsAdmin
    @GetMapping(value = "/users/{username}")
    public User getUser(@Parameter(description = "username of user", required = true) @PathVariable String username);

    @Operation(summary = "Gets all roles", description = "Gets all roles.", security = {
            @SecurityRequirement(name = "bearerToken") }, tags = { "authentication" })
    @IsAdmin
    @GetMapping(value = "/authorities")
    public Iterable<Authority> getAuthorities();

    @Operation(summary = "Gets all users with role name", description = "Gets all users with role name.", security = {
            @SecurityRequirement(name = "bearerToken") }, tags = { "authentication" })
    @IsAdmin
    @GetMapping(value = "/authorities/{role}")
    public Iterable<User> getUsersByAuthority(
            @Parameter(description = "role name", required = true) @PathVariable Role role);

    @Operation(summary = "Gets roles wise users list", description = "Gets roles wise users list.", security = {
            @SecurityRequirement(name = "bearerToken") }, tags = { "authentication" })
    @IsAdmin
    @GetMapping(value = "/roleswiseusers")
    public Iterable<User> getRoleWiseUsersList();
}
