package dev.animals.service;

import dev.animals.service.auth.AuthDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String generateJwtToken(Authentication authentication) {

    AuthDetailsImpl userPrincipal = (AuthDetailsImpl) authentication.getPrincipal();

    return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
      .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
      .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
  }

  public boolean validateJwtToken(String jwt) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
      return true;
    } catch (MalformedJwtException e) {
      System.err.println(e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    }

    return false;
  }

  public String getUserNameFromJwtToken(String jwt) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt.replace("Bearer ", "")).getBody().getSubject();
  }
}
