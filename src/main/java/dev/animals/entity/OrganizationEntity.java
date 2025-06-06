package dev.animals.entity;

import dev.animals.entity.animal.AnimalEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "organizations")
public class OrganizationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String passportSeries;

  @Column(nullable = false)
  private String passportNumber;

  @Column(nullable = false)
  private String phoneNumber;

  @JoinColumn(name = "auth_username", nullable = false)
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private AuthEntity auth;

  @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
  private List<AnimalEntity> animals;

  @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
  private Set<ChatEntity> chats = new HashSet<>();

  @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
  private List<MessageEntity> messages = new ArrayList<>();

  public OrganizationEntity(Long id,
                            String name,
                            String city,
                            String passportSeries,
                            String passportNumber,
                            String phoneNumber) {
    this.id = id;
    this.name = name;
    this.city = city;
    this.passportSeries = passportSeries;
    this.passportNumber = passportNumber;
    this.phoneNumber = phoneNumber;
  }

  public OrganizationEntity(String name,
                            String city,
                            String passportSeries,
                            String passportNumber,
                            String phoneNumber) {
    this(null, name, city, passportSeries, passportNumber, phoneNumber);
  }
}
