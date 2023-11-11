package dev.pethaven.services;

import dev.pethaven.dto.PageDataDTO;
import dev.pethaven.dto.PetDTO;
import dev.pethaven.entity.*;
import dev.pethaven.mappers.PetMapper;
import dev.pethaven.pojo.FilterFields;
import dev.pethaven.pojo.SavePet;
import dev.pethaven.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
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
    public Page<PetDTO> getFilteredPets(int page, int size, FilterFields filterFields){
        PetSpecification specification = new PetSpecification(filterFields);
        return petRepository.findAll(specification, PageRequest.of(page,size)).map(petMapper::toDTO);
    }
    public void addPet(Principal user, SavePet newPetInfo){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Pet tempPet = petMapper.toEntity(newPetInfo);
        Pet newPet = new Pet(null, tempPet.getName(), tempPet.getGender(), tempPet.getTypePet(),
                tempPet.getBirthDay(), tempPet.getBreed(), tempPet.getDescription(), PetStatus.ACTIVE,
                organizationRepository.findByAuthId(authRepository.findByUsername(user.getName()).get().getId()));
        petRepository.save(newPet);
        String bucketName = newPet.getId().toString() + "-" + newPet.getTypePet().toString().toLowerCase();
        try {
            minioService.createBucket(bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!newPetInfo.getFiles().isEmpty()) {
            minioService.uploadFile(newPetInfo.getFiles(), bucketName);
            newPetInfo.getFiles().forEach(file -> {
                PetPhotos petPhotos = new PetPhotos(null, file.getOriginalFilename(), newPet);
                petPhotosRepository.save(petPhotos);
            });
        }
    }
    public void updatePet(Long petId, SavePet updatedPet){
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
    }
    public void deletePet(Long id){
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
    }
    public void adoptPet(String username, Pet pet){
        Auth currentAuth = (authRepository.findByUsername(username)).get();
        User user = userRepository.findByAuthId(currentAuth.getId());
        user.getPetSet().remove(pet);
        pet.setUser(user);
        pet.getUserSet().clear();
        pet.setStatus(PetStatus.ADOPTED);
        userRepository.save(user);
        petRepository.save(pet);
    }
    public void deleteRequestUser(Long petId, String username){
        User user = userRepository.findByAuthId(authRepository.findByUsername(username).get().getId());
        user.getPetSet().remove(petRepository.findById(petId).get());
        userRepository.save(user);
    }
    public void updateStatusPet(Long petId, String newStatus){
        Pet updatedpet = petRepository.findById(petId).get();
        updatedpet.setStatus(PetStatus.valueOf(newStatus));
        petRepository.save(updatedpet);
    }
}
