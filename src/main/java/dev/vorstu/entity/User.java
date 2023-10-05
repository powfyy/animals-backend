package dev.vorstu.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table (name= "users")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne (cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private Auth auth;

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }
}
