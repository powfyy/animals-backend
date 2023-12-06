package dev.pethaven.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateLastMessage = LocalDateTime.now();

    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;
    @Column(name="user_id")
    private Long userId;

    @JoinColumn(name = "organization_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    private Organization organization;
    @Column(name="organization_id")
    private Long organizationId;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    public void setOrganization(Organization organization) {
        setOrganizationId(organization.getId());
        this.organization = organization;
    }

    public void setUser(User user) {
        setUserId(user.getId());
        this.user = user;
    }
}
