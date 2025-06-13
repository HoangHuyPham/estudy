package vn.edu.hcmuaf.be.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import vn.edu.hcmuaf.be.config.CustomConfig;
import vn.edu.hcmuaf.be.entity.User;

@Component
public class JwtUtil {

    @Autowired
    public CustomConfig config;

    public String generateToken(User user){
        String sub = user.getId().toString();
        long iat = Instant.now().toEpochMilli();
        long exp = Instant.now().plus(5, ChronoUnit.DAYS).toEpochMilli();

        return Jwts.builder()
                .setSubject(sub)
                .setIssuedAt(Date.from(Instant.ofEpochMilli(iat)))
                .setExpiration(Date.from(Instant.ofEpochMilli(exp)))
                .signWith(Keys.hmacShaKeyFor(config.getJwt().getKey().getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token){
        return !isExpired(token);
    }

    public UUID getSubFrom(String token){
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(config.getJwt().getKey().getBytes())).build();
        String sub = jwtParser.parseClaimsJws(token).getBody().getSubject();
        if (sub == null) {
            return null;
        }
        return UUID.fromString(sub);
    }

    public boolean isExpired(String token){
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(config.getJwt().getKey().getBytes())).build();
        Date expDate = jwtParser.parseClaimsJws(token).getBody().getExpiration();
        return expDate.before(Date.from(Instant.now()));
    }
}
