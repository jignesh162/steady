package com.steady.nifty.strategy.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.steady.nifty.strategy.entity.Authority;
import com.steady.nifty.strategy.entity.Role;
import com.steady.nifty.strategy.entity.User;
import com.steady.nifty.strategy.payload.request.LoginRequest;
import com.steady.nifty.strategy.payload.request.SignupRequest;
import com.steady.nifty.strategy.payload.request.UserUpdateRequest;
import com.steady.nifty.strategy.payload.response.JwtResponse;
import com.steady.nifty.strategy.repository.AuthorityRepository;
import com.steady.nifty.strategy.repository.UserRepository;
import com.steady.nifty.strategy.security.jwt.JwtUtils;
import com.steady.nifty.strategy.util.Translator;

@Transactional
@Service
@Validated
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder pwEncoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder pwEncoder,
            UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.pwEncoder = pwEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
    }

    @Override
    public User createUser(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new EntityExistsException(
                    Translator.toLocale("entityExistsExceptionMessage", signupRequest.getUsername()));
        }
        User user = new User(signupRequest.getUsername(), pwEncoder.encode(signupRequest.getPassword()),
                signupRequest.isEnabled(), getAuthorities(signupRequest.getRoles()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserUpdateRequest updateRequest) {
        User user = userRepository.findByUsername(updateRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        Translator.toLocale("entityNotFoundExceptionMessage", updateRequest.getUsername())));
        String newPassword = StringUtils.isEmpty(updateRequest.getPassword()) ? null
                : pwEncoder.encode(updateRequest.getPassword());
        user.setPayload(updateRequest.isEnabled(), newPassword, getAuthorities(updateRequest.getRoles()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(Translator.toLocale("entityNotFoundExceptionMessage", username)));
        userRepository.delete(user);
    }

    private Set<Authority> getAuthorities(Set<Role> roles) {
        Set<Authority> authorities = new HashSet<>();
        for (Role role : roles) {
            Authority authority = authorityRepository.findByAuthority(role).orElseThrow(
                    () -> new EntityNotFoundException(Translator.toLocale("entityNotFoundExceptionMessage", role)));
            authorities.add(authority);
        }
        return authorities;
    }

    @Override
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(Translator.toLocale("entityNotFoundExceptionMessage", username)));
    }

    @Override
    public Iterable<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }

    @Override
    public Iterable<User> getUsersByAuthority(Role role) {
        return userRepository.findByAuthorities_Authority(role);
    }
}
