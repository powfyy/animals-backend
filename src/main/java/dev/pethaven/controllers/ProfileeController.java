package dev.pethaven.controllers;


import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.PetDTO;
import dev.pethaven.dto.UserDTO;
import dev.pethaven.entity.*;
import dev.pethaven.mappers.OrganizationMapper;
import dev.pethaven.mappers.PetMapper;
import dev.pethaven.mappers.UserMapper;
import dev.pethaven.pojo.MessageResponse;
import dev.pethaven.pojo.SavePet;
import dev.pethaven.repositories.*;
import dev.pethaven.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileeController {
    @Autowired
    AuthRepository authRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    PetPhotosRepository petPhotosRepository;
    @Autowired
    PetMapper petMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    MinioService minioService;

    @GetMapping("/user")
    public UserDTO getUserInfo(Principal user) {
        Auth currentAuth = (authRepository.findByUsername(user.getName())).get();
        return userMapper.toDTO(userRepository.findByAuthId(currentAuth.getId()));
    }

    @GetMapping("/organization")
    public OrganizationDTO getOrganization(Principal user) {
        Auth currentAuth = (authRepository.findByUsername(user.getName())).get();
        return organizationMapper.toDTO(organizationRepository.findByAuthId(currentAuth.getId()));
    }

    @PutMapping("/organization")
    public void updateOrganization(Principal user, @RequestBody OrganizationDTO updatedOrganization) {
        Auth currentAuth = (authRepository.findByUsername(user.getName())).get();
        Organization organization = organizationRepository.findByAuthId(currentAuth.getId());
        organizationMapper.updateOrganization(updatedOrganization, organization);
        organizationRepository.save(organization);
    }

    @DeleteMapping("/organization")
    public void deleteOrganization(Principal user) {
        Auth currentAuth = (authRepository.findByUsername(user.getName())).get();
        organizationRepository.deleteById(organizationRepository.findByAuthId(currentAuth.getId()).getId());
    }

    @GetMapping("/organization/pets")
    public List<PetDTO> getAllPets(Principal user) {
        Auth currentAuth = (authRepository.findByUsername(user.getName())).get();
        List<Pet> petsArray = petRepository.findByOrganizationId(organizationRepository.findByAuthId(currentAuth.getId()).getId());
        return petsArray.stream()
                .map(el -> petMapper.toDTO(el))
                .collect(Collectors.toList());
    }

    @PostMapping("/organization/pets")
    @Transactional
    public ResponseEntity<?> addPet(Principal user, @ModelAttribute SavePet newPetInfo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Pet tempPet = petMapper.toEntity(newPetInfo);
        Pet newPet = new Pet(null, tempPet.getName(), tempPet.getGender(), tempPet.getTypePet(),
                tempPet.getBirthDay(), tempPet.getBreed(), tempPet.getDescription(), PetStatus.ACTIVE,
                organizationRepository.findByAuthId(authRepository.findByUsername(user.getName()).get().getId()));
        petRepository.save(newPet);
        String bucketName = newPet.getId().toString() + "-" + newPet.getTypePet().toString().toLowerCase();
        try {
            minioService.createBucket(bucketName);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        if (!newPetInfo.getFiles().isEmpty()) {
            minioService.uploadFile(newPetInfo.getFiles(), bucketName);
            newPetInfo.getFiles().forEach(file -> {
                PetPhotos petPhotos = new PetPhotos(null, file.getOriginalFilename(), newPet);
                petPhotosRepository.save(petPhotos);
            });
        }
        return ResponseEntity.ok().body(new MessageResponse("Pet created"));
    }

    @PutMapping ("/organization/pets/{id}")
    @Transactional
    public ResponseEntity <?> updatePet (@PathVariable("id") Long petId, @ModelAttribute SavePet updatedPet){
        String bucketName = petId.toString() + "-" + updatedPet.getTypePet().toLowerCase();
        Pet oldPet = petRepository.findById(petId).get();
        if (!updatedPet.getDeletedPhotoRefs().isEmpty()){
            minioService.removeFiles(updatedPet.getDeletedPhotoRefs(),bucketName);
            updatedPet.getDeletedPhotoRefs().forEach(deletedRef -> {
                petPhotosRepository.deleteByPhotoRef(deletedRef);
            });
        }
        if(!updatedPet.getFiles().isEmpty()){
            minioService.uploadFile(updatedPet.getFiles(), bucketName);
            updatedPet.getFiles().forEach(file -> {
                PetPhotos petPhotos = new PetPhotos(null, file.getOriginalFilename(), oldPet);
                petPhotosRepository.save(petPhotos);
            });
        }
        petMapper.updatePet(updatedPet, oldPet);
        petRepository.save(oldPet);
        return ResponseEntity.ok().body(new MessageResponse("Pet updated"));
    }
    @DeleteMapping("/organization/pets/{id}")
    @Transactional
    public ResponseEntity<?> deletePet(@PathVariable("id") Long id) {
        Pet pet =petRepository.findById(id).get();
        String bucketName = pet.getId() + "-" + pet.getTypePet().toString().toLowerCase();
        if(!pet.getPetPhotos().isEmpty()){
            try{
                minioService.removeFiles(bucketName, pet.getPetPhotos());
            }catch (Exception e) {
                System.out.println("Error deleting files: " + e.getMessage());
            }
        }
        try {
            minioService.removeBucket(bucketName);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e){
            System.out.println("Error deleting bucket: "+ e.getMessage());
        }
        petRepository.deleteById(id);
        petPhotosRepository.deleteByPetId(id);
        return ResponseEntity.ok().body(new MessageResponse("Pet deleted."));
    }

    @PatchMapping("/organization/pets/{id}/status")
    public ResponseEntity<?> updateStatusPet(@PathVariable("id") Long petId,
                                             @RequestParam(value = "newStatus") String newStatus) {
        Pet updatedpet = petRepository.findById(petId).get();
        updatedpet.setStatus(PetStatus.valueOf(newStatus));
        petRepository.save(updatedpet);
        return ResponseEntity.ok().body(new MessageResponse("Status updated"));
    }


    @PostMapping("/organization/pets/{id}/adopt")
    public ResponseEntity<?> adoptPet(@PathVariable("id") Long petId, @RequestBody String username) {
        Pet pet = petRepository.findById(petId).get();
        if (pet.getStatus() == PetStatus.FREEZE) {
            Auth currentAuth = (authRepository.findByUsername(username)).get();
            User user = userRepository.findByAuthId(currentAuth.getId());
            user.getPetSet().remove(pet);
            pet.setUser(user);
            pet.getUserSet().clear();
            pet.setStatus(PetStatus.ADOPTED);
            userRepository.save(user);
            petRepository.save(pet);
            return ResponseEntity.ok(new MessageResponse("Питомец усыновлен"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Неверный статус питомца. Требуется 'FREEZE'."));
    }

    @GetMapping("/organization/pets/{id}/userRequest")
    public Set<UserDTO> getUserRequsts(@PathVariable("id") Long petId) {
        return userMapper.toDtoSet(petRepository.findById(petId).get().getUserSet());
    }

    @DeleteMapping("/organization/pets/{id}/delUserRequest")
    public ResponseEntity<?> deleteRequestUser(@PathVariable("id") Long petId,
                                               @RequestParam(value = "username") String username) {
        User user = userRepository.findByAuthId(authRepository.findByUsername(username).get().getId());
        user.getPetSet().remove(petRepository.findById(petId).get());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Заявка удалена."));
    }
}
