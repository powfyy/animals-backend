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

    @JoinColumn(name = "chat_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(targetEntity = Chat.class, fetch = FetchType.LAZY)
    private Chat chat;
    @Column(name="chat_id")
    private Long chatId;

    @JoinColumn(name = "organization_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    private Organization organization;
    @Column(name="organization_id")
    private Long organizationId;

    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;
    @Column(name="user_id")
    private Long userId;

    public Message(Long id, String message, LocalDateTime date, Long chatId) {

        this.id = id;
        this.message = message;
        this.date = date;
        this.chatId = chatId;
    }

    public Message(String message, LocalDateTime date, Long chatId) {
        this(null, message, date, chatId);
    }

    public void setChat(Chat chat) {
        setChatId(chat.getId());
        this.chat = chat;
    }

    public void setOrganization(Organization organization) {
        setOrganizationId(organization.getId());
        this.organization = organization;
    }

    public void setUser(User user) {
        setUserId(user.getId());
        this.user = user;
    }
}
