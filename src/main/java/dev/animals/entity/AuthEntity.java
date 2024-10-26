package dev.animals.entity;

import dev.animals.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "auth")
public class AuthEntity {

  @Id
  @Column(nullable = false)
  private String username;

  @Enumerated(EnumType.STRING)
  private Role role;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private boolean enable = true;

  public AuthEntity(String username, Role role, String password) {
    this.username = username;
    this.role = role;
    this.password = password;
  }
}
