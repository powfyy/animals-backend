package dev.animals.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
public class MessageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String message;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime date;

  @ManyToOne
  @JoinColumn(name = "chat_id", nullable = false)
  private ChatEntity chat;

  @ManyToOne
  @JoinColumn(name = "organization_id", nullable = false)
  private OrganizationEntity organization;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  public MessageEntity(Long id, String message, ChatEntity chat) {
    this.id = id;
    this.message = message;
    this.chat = chat;
  }

  public MessageEntity(String message, ChatEntity chat) {
    this(null, message, chat);
  }
}
