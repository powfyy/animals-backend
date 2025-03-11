package dev.animals.repository.animal;

import dev.animals.entity.animal.AnimalPhotosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AnimalPhotosRepository extends JpaRepository<AnimalPhotosEntity, Long> {

  void deleteAllByPhotoRefIn(Collection<String> photoRefs);
}
