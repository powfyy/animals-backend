package dev.pethaven.services;

import dev.pethaven.dto.PetDTO;
import dev.pethaven.dto.UserDTO;
import dev.pethaven.entity.*;
import dev.pethaven.enums.PetStatus;
import dev.pethaven.exception.InvalidPetStatusException;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.PetMapper;
import dev.pethaven.dto.FilterFields;
import dev.pethaven.dto.SavePet;
import dev.pethaven.mappers.UserMapper;
import dev.pethaven.repositories.*;
import dev.pethaven.specifications.PetSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    UserService userService;
    @Autowired
    MinioService minioService;
    @Autowired
    PetPhotosRepository petPhotosRepository;
    @Autowired
    PetMapper petMapper;

    @Autowired
    UserMapper userMapper;

    public Page<PetDTO> getFilteredPets(int page, int size, FilterFields filterFields) {
        PetSpecification specification = new PetSpecification(filterFields);
        return petRepository.findAll(specification, PageRequest.of(page, size)).map(petMapper::toDTO);
    }

    public List<PetDTO> getAllPetsCurrentOrganization(String organizationUsername) {
        List<Pet> petsArray = petRepository.findByOrganizationUsername(organizationUsername);
        return petsArray.stream()
                .map(el -> petMapper.toDTO(el))
                .collect(Collectors.toList());
    }

    public PetDTO getPetDTOById(@NotNull Long petId) {
        return petMapper.toDTO(petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet is not found")));
    }

    public Pet findById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet is not found"));
    }

    @Transactional
    public PetDTO addPet(String organizationUsername, @Valid SavePet newPetInfo) {
        Pet tempPet = petMapper.toEntity(newPetInfo);
        Pet newPet = new Pet(
                tempPet.getName(),
                tempPet.getGender(),
                tempPet.getTypePet(),
                tempPet.getBirthDay(),
                tempPet.getBreed(),
                tempPet.getDescription(),
                PetStatus.ACTIVE);
        newPet.setOrganizationId(organizationService.findByUsername(organizationUsername).getId());
        petRepository.save(newPet);
        String bucketName = newPet.getId().toString() + "-" + newPet.getTypePet().toString().toLowerCase();
        minioService.createBucket(bucketName);
        if (!newPetInfo.getFiles().isEmpty()) {
            minioService.uploadFile(newPetInfo.getFiles(), bucketName);
            List<PetPhotos> petPhotosList = new ArrayList<>();
            newPetInfo.getFiles().forEach(file -> {
                PetPhotos petPhotos = new PetPhotos(file.getOriginalFilename(), newPet.getId());
                petPhotosList.add(petPhotos);
            });
            petPhotosRepository.saveAll(petPhotosList);
        }
        return petMapper.toDTO(newPet);
    }

    @Transactional
    public PetDTO updatePet(@NotNull(message = "Id cannot be null") Long petId, @Valid SavePet updatedPet) {
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
            List<PetPhotos> petPhotosList = new ArrayList<>();
            updatedPet.getFiles().forEach(file -> {
                PetPhotos petPhotos = new PetPhotos(file.getOriginalFilename(), oldPet.getId());
                petPhotosList.add(petPhotos);
            });
            petPhotosRepository.saveAll(petPhotosList);
        }
        petMapper.updatePet(updatedPet, oldPet);
        petRepository.save(oldPet);
        return petMapper.toDTO(oldPet);
    }

    @Transactional
    public void deletePet(@NotNull(message = "Id cannot be null") Long id) {
        Pet pet = findById(id);
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
                         @NotNull(message = "Pet cannot be null") Long petId) {
        Pet pet = findById(petId);
        if (pet.getStatus() != PetStatus.FREEZE) {
            throw new InvalidPetStatusException("Incorrect pet status. It is required to be FREEZE");
        }
        User user = userService.findByUsername(username);
//        user.getPetSet().remove(pet);
        pet.setUser(user);
        pet.getUserSet().clear();
        pet.setStatus(PetStatus.ADOPTED);
        petRepository.save(pet);
    }

    @Transactional
    public void deleteRequestUser(@NotNull(message = "Id cannot be null") Long petId,
                                  @Size(min = 4, message = "username must be minimum 4 chars") String username) {
        User user = userService.findByUsername(username);
        user.getPetSet().remove(findById(petId));
        userService.save(user);
    }

    public void updateStatusPet(@NotNull(message = "Id can't be null") Long petId,
                                @NotEmpty(message = "Status can't be empty") String newStatus) {
        Pet updatedPet = findById(petId);
        if (updatedPet.getStatus().canTransitionTo(PetStatus.valueOf(newStatus))) {
            updatedPet.setStatus(PetStatus.valueOf(newStatus));
            petRepository.save(updatedPet);
        } else {
            throw new InvalidParameterException("Invalid status");
        }
    }

    public Map<String, Boolean> checkRequest(String username, @NotNull(message = "Id cannot be null") Long petId) {
        User user = userService.findByUsername(username);
        if (user.getPetSet().contains(findById(petId))) {
            return Collections.singletonMap("isThereRequest", true);
        }
        return Collections.singletonMap("isThereRequest", false);
    }

    public Set<UserDTO> getUserRequsts(@NotNull(message = "Id can't be null") Long petId) {
        return userMapper.toDtoSet(findById(petId).getUserSet());
    }

    public void requestForPet(String username, @NotNull(message = "Id cannot be null") Long petId) {
        User currentUser = userService.findByUsername(username);
        currentUser.getPetSet().add(findById(petId));
        userService.save(currentUser);
    }
}
