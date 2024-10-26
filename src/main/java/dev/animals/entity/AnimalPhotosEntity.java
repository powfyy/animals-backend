package dev.animals.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animal_photos")
public class AnimalPhotosEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  String photoRef;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "animal_id", nullable = false, insertable = false, updatable = false)
  private AnimalEntity animal;

  public AnimalPhotosEntity(String photoRef, AnimalEntity animal) {
    this.photoRef = photoRef;
    this.animal = animal;
  }
}
