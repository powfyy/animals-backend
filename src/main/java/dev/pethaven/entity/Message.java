package dev.pethaven.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private LocalDateTime date;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Chat chat;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private User user;

    public Message(Long id, String message, LocalDateTime date, Chat chat) {
        this.id = id;
        this.message = message;
        this.date = date;
        this.chat = chat;
    }

    public Message(String message, LocalDateTime date, Chat chat) {
        this(null, message, date, chat);
    }
}
