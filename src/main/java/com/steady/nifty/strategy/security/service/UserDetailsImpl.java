package com.steady.nifty.strategy.security.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.steady.nifty.strategy.entity.User;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;
    @Getter(onMethod = @__({ @Override }))
    private String username;
    // TODO - is this needed? This should never be returned.
    @JsonIgnore
    @Getter(onMethod = @__({ @Override }))
    private String password;
    @Getter(onMethod = @__({ @Override }))
    private boolean enabled;
    @Getter(onMethod = @__({ @Override }))
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority().name())).collect(Collectors.toList());

        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), user.isEnabled(), authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO
        return true;
    }
}
