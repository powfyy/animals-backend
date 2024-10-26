package dev.animals.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String role;

  public JwtResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}
