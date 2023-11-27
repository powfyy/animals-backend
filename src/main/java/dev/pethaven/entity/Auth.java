package dev.pethaven.entity;

import dev.pethaven.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "auth", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Getter
@Setter
@NoArgsConstructor
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean enable;

    public Auth(Long id, String username, Role role, String password, boolean enable) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.password = password;
        this.enable = enable;
    }

    public Auth(String username, Role role, String password, boolean enable) {
        this(null, username, role, password, enable);
    }
}
