package dev.animals.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "chats")
public class ChatEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_id", nullable = false, insertable = false, updatable = false)
  private OrganizationEntity organization;

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
  private List<MessageEntity> messages;

  public ChatEntity(OrganizationEntity organization, UserEntity user) {
    this.organization = organization;
    this.user = user;
  }
}
