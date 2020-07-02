package com.steady.nifty.strategy.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.steady.nifty.strategy.security.jwt.AuthTokenFilter;
import com.steady.nifty.strategy.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // disable CSRF protection because it doesn't make sense on REST service
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // never create
                                                                                                  // HttpSession
                .authorizeRequests().antMatchers("/auth/login/**").permitAll().and() // allow access to login for all
                .authorizeRequests().antMatchers("/h2/**").permitAll().and() // allow h2 console access to admins
                .authorizeRequests().antMatchers("/swagger-ui/**").permitAll().and() // TODO!!! REMOVE THIS
                .authorizeRequests().antMatchers("/v3/**").permitAll().and() // TODO!!! REMOVE THIS
                .authorizeRequests().antMatchers("/swagger-ui.html").permitAll() // TODO!!! REMOVE THIS
                .anyRequest().authenticated().and() // all other urls can be access by any authenticated role
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and() // Return 401 if
                                                                                              // authentication fails.
                .cors().and() // by default uses a Bean by the name of corsConfigurationSource
                .headers().frameOptions().sameOrigin(); // allow use of frame to same origin urls

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * This is required by the AuthController.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
