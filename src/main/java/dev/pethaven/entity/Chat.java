package dev.pethaven.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

}
