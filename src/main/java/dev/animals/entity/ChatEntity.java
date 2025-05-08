package dev.animals.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@FieldNameConstants
@Table(name = "chats")
public class ChatEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String lastMessageText;
  private LocalDateTime lastMessageDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_id", nullable = false)
  private OrganizationEntity organization;

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
  private List<MessageEntity> messages = new ArrayList<>();

  public ChatEntity(OrganizationEntity organization, UserEntity user) {
    this.organization = organization;
    this.user = user;
  }
}
