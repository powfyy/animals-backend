package dev.pethaven.services;

import dev.pethaven.dto.PetDTO;
import dev.pethaven.entity.*;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.PetMapper;
import dev.pethaven.pojo.FilterFields;
import dev.pethaven.pojo.SavePet;
import dev.pethaven.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    MinioService minioService;
    @Autowired
    PetPhotosRepository petPhotosRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PetMapper petMapper;

    public Page<PetDTO> getFilteredPets(int page, int size, FilterFields filterFields) {
        PetSpecification specification = new PetSpecification(filterFields);
        return petRepository.findAll(specification, PageRequest.of(page, size)).map(petMapper::toDTO);
    }

    public List<PetDTO> getAllPetsCurrentOrganization(Principal principal) {
        Auth currentAuth = (authRepository
                .findByUsername(principal.getName()))
                .orElseThrow(() -> new NotFoundException("Auth not found"));
        List<Pet> petsArray = petRepository.findByOrganizationId(organizationRepository
                .findByAuthId(currentAuth.getId())
                .orElseThrow(() -> new NotFoundException("Organization not found"))
                .getId());
        return petsArray.stream()
                .map(el -> petMapper.toDTO(el))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addPet(Principal user, @Valid SavePet newPetInfo) {
        Pet tempPet = petMapper.toEntity(newPetInfo);
        Pet newPet = new Pet(null, tempPet.getName(), tempPet.getGender(), tempPet.getTypePet(),
                tempPet.getBirthDay(), tempPet.getBreed(), tempPet.getDescription(), PetStatus.ACTIVE,
                organizationRepository.findByAuthId(authRepository
                                .findByUsername(user.getName())
                                .orElseThrow(() -> new NotFoundException("Auth not found"))
                                .getId())
                        .orElseThrow(() -> new NotFoundException("Organization not found")));
        petRepository.save(newPet);
        String bucketName = newPet.getId().toString() + "-" + newPet.getTypePet().toString().toLowerCase();
        minioService.createBucket(bucketName);
        if (!newPetInfo.getFiles().isEmpty()) {
            minioService.uploadFile(newPetInfo.getFiles(), bucketName);
            newPetInfo.getFiles().forEach(file -> {
                PetPhotos petPhotos = new PetPhotos(null, file.getOriginalFilename(), newPet);
                petPhotosRepository.save(petPhotos);
            });
        }
    }

    @Transactional
    public void updatePet(@NotNull(message = "Id cannot be null") Long petId,@Valid SavePet updatedPet) {
        String bucketName = petId.toString() + "-" + updatedPet.getTypePet().toLowerCase();
        Pet oldPet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("Pet not found"));
        if (!updatedPet.getDeletedPhotoRefs().isEmpty()) {
            minioService.removeFiles(updatedPet.getDeletedPhotoRefs(), bucketName);
            updatedPet.getDeletedPhotoRefs().forEach(deletedRef -> {
                petPhotosRepository.deleteByPhotoRef(deletedRef);
            });
        }
        if (!updatedPet.getFiles().isEmpty()) {
            minioService.uploadFile(updatedPet.getFiles(), bucketName);
            updatedPet.getFiles().forEach(file -> {
                PetPhotos petPhotos = new PetPhotos(null, file.getOriginalFilename(), oldPet);
                petPhotosRepository.save(petPhotos);
            });
        }
        petMapper.updatePet(updatedPet, oldPet);
        petRepository.save(oldPet);
    }

    @Transactional
    public void deletePet(@NotNull(message = "Id cannot be null") Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new NotFoundException("Pet not found"));
        String bucketName = pet.getId() + "-" + pet.getTypePet().toString().toLowerCase();
        if (!pet.getPetPhotos().isEmpty()) {
            minioService.removeFiles(bucketName, pet.getPetPhotos());
        }
        minioService.removeBucket(bucketName);
        petRepository.deleteById(id);
        petPhotosRepository.deleteByPetId(id);
    }

    @Transactional
    public void adoptPet(@Size(min = 4, message = "username must be minimum 4 chars") String username,
                         @NotNull(message = "Pet cannot be null") Pet pet) {
        Auth currentAuth = (authRepository
                .findByUsername(username))
                .orElseThrow(() -> new NotFoundException("Auth not found"));
        User user = userRepository
                .findByAuthId(currentAuth.getId())
                .orElseThrow(() -> new NotFoundException("Auth not found"));
        user.getPetSet().remove(pet);
        pet.setUser(user);
        pet.getUserSet().clear();
        pet.setStatus(PetStatus.ADOPTED);
        userRepository.save(user);
        petRepository.save(pet);
    }

    @Transactional
    public void deleteRequestUser(@NotNull(message = "Id cannot be null") Long petId,
                                  @Size(min = 4, message = "username must be minimum 4 chars") String username) {
        User user = userRepository
                .findByAuthId(authRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new NotFoundException("Auth not found"))
                        .getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.getPetSet().remove(petRepository
                .findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet not found")));
        userRepository.save(user);
    }

    public void updateStatusPet(@NotNull(message = "Id cannot be null") Long petId, String newStatus) {
        Pet updatedPet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet not found"));
        updatedPet.setStatus(PetStatus.valueOf(newStatus));
        petRepository.save(updatedPet);
    }

}
