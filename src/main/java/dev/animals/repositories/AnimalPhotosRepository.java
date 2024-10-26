package dev.animals.repositories;

import dev.animals.entity.AnimalPhotosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalPhotosRepository extends JpaRepository<AnimalPhotosEntity, Long> {

  void deleteByAnimalId(Long animalId);

  void deleteByPhotoRef(String photoRef);
}
