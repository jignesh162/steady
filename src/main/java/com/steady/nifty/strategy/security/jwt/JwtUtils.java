package com.steady.nifty.strategy.security.jwt;

import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.steady.nifty.strategy.exception.Fail2BanException;
import com.steady.nifty.strategy.security.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtUtils {

    private String secret;
    private int expirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + this.expirationMs))
                .signWith(SignatureAlgorithm.HS512, this.secret).compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            // This is normal, we don't want to ban users for having expired credentials.
            log.debug("JWT token is expired", e);
            return false;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty", e);
        }
        throw new Fail2BanException();
    }
}
