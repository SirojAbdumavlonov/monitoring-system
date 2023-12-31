package com.example.authorization.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET_KEY = "30M18gaU4TRNnOPIcsOsp6BoCrvkxUZ2rej8OuQILnahRGarCku0eAjl5QPKpeI7gMdWSZ+9523gly72Efs4CQ==";
    private static final long EXPIRATION_TIME = 864_000_000;

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

//    public String generateToken(UserDetails userDetails){
//        return generateToken(new HashMap<>(), userDetails);
//    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    public boolean isTokenExpired(String token){
        if(extractExpiration(token).before(new Date())){
            throw new RuntimeException("Incorrect data!");
        }
        return false;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }


    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


//    public static String updateAuthorities(String token, Collection<? extends GrantedAuthority> newAuthorities) {
//        Jws<Claims> claimsJws =
//                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
//        Claims claims = claimsJws.getBody();
//
//        List<String> roles = newAuthorities.stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        // Update the 'authorities' claim in the JWT
//        claims.put("authorities", roles);
//
//        // Re-encode the JWT with updated claims
//        return Jwts.builder()
//                .setClaims(claims)
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }


}
