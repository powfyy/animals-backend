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

    public List<PetDTO> getAllPetsCurrentOrganization(Principal principal) {
        List<Pet> petsArray = petRepository.findByOrganizationUsername(principal.getName());
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
    public PetDTO addPet(Principal principal, @Valid SavePet newPetInfo) {
        Pet tempPet = petMapper.toEntity(newPetInfo);
        Pet newPet = new Pet(
                tempPet.getName(),
                tempPet.getGender(),
                tempPet.getTypePet(),
                tempPet.getBirthDay(),
                tempPet.getBreed(),
                tempPet.getDescription(),
                PetStatus.ACTIVE,
                //todo сохранение по id, а не по организации JoinColumn нужно менять
                organizationService.findByUsername(principal.getName())
        );

        String bucketName = newPet.getId().toString() + "-" + newPet.getTypePet().toString().toLowerCase();
        minioService.createBucket(bucketName);
        if (!newPetInfo.getFiles().isEmpty()) {
            minioService.uploadFile(newPetInfo.getFiles(), bucketName);
            List<PetPhotos> petPhotosList = new ArrayList<>();
            newPetInfo.getFiles().forEach(file -> {
                petPhotosList.add(new PetPhotos(file.getOriginalFilename(), newPet));
            });
            petPhotosRepository.saveAll(petPhotosList);
        }
        petRepository.save(newPet);
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
            updatedPet.getFiles().forEach(file -> {
                //todo
                PetPhotos petPhotos = new PetPhotos(null, file.getOriginalFilename(), oldPet);
                petPhotosRepository.save(petPhotos);
            });
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
        user.getPetSet().remove(pet);
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

    public Map<String, Boolean> checkRequest(Principal principal, @NotNull(message = "Id cannot be null") Long petId) {
        User user = userService.findByUsername(principal.getName());
        if (user.getPetSet().contains(findById(petId))) {
            return Collections.singletonMap("isThereRequest", true);
        }
        return Collections.singletonMap("isThereRequest", false);
    }

    public Set<UserDTO> getUsersRequsts(@NotNull(message = "Id can't be null") Long petId) {
        return userMapper.toDtoSet(findById(petId).getUserSet());
    }

    public void requestForPet(Principal principal, @NotNull(message = "Id cannot be null") Long petId) {
        User currentUser = userService.findByUsername(principal.getName());
        currentUser.getPetSet().add(findById(petId));
        userService.save(currentUser);
    }
}
