package dev.pethaven.repositories;

import dev.pethaven.entity.PetPhotos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetPhotosRepository extends JpaRepository <PetPhotos, Long> {
    public void deleteByPetId(Long petId);
    public void deleteByPhotoRef(String photoRef);
}
