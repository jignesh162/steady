package com.steady.nifty.strategy.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.steady.nifty.strategy.entity.User;
import com.steady.nifty.strategy.repository.UserRepository;
import com.steady.nifty.strategy.util.Translator;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(Translator.toLocale("userNotFoundExceptionMessage", username)));

        return UserDetailsImpl.build(user);
    }
}
