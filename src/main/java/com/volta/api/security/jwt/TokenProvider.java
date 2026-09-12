package com.volta.api.security.jwt;

import com.volta.api.security.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {
    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.key}")
    private String key;

    //gerar um token
    public String gerarToken(Authentication authentication){
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return buildToken(user);
    }

    private String buildToken(AuthenticatedUser user){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.id().toString())
                .claim("companyId", user.companyId().toString())
                .claim("role", user.role())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    //validar um token
    public boolean isTokenValid(String token){
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //extrair informações do token
    public String getUsername(String token){
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token){
        //validar assinatura
        //validar expiração
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
