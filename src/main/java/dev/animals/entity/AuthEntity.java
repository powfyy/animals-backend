package dev.animals.entity;

import dev.animals.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
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
