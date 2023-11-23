package dev.pethaven.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="chats")
@Data
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Organization organization;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List <Message> messages;

    public Chat(Long id, User user, Organization organization) {
        this.id = id;
        this.user = user;
        this.organization = organization;
    }
}
