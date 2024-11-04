package dev.animals.entity;

import dev.animals.entity.animal.AnimalEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table (name= "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String phoneNumber;

    @JoinColumn(name = "auth_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private AuthEntity auth;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "user_animal",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    private Set<AnimalEntity> animalSet = new HashSet<>();

    @OneToMany (mappedBy = "user",cascade = CascadeType.ALL)
    private Set<ChatEntity> chats = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MessageEntity> messages = new ArrayList<>();

  public UserEntity(Long id, String name, String lastname, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
    }

  public UserEntity(String name, String lastname, String phoneNumber) {
        this(null, name, lastname, phoneNumber);
    }

}
