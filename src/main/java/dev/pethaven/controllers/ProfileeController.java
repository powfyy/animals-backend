package dev.pethaven.controllers;


import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.PetDTO;
import dev.pethaven.dto.UserDTO;
import dev.pethaven.entity.*;
import dev.pethaven.mappers.OrganizationMapper;
import dev.pethaven.mappers.PetMapper;
import dev.pethaven.mappers.UserMapper;
import dev.pethaven.pojo.MessageResponse;
import dev.pethaven.repositories.AuthRepository;
import dev.pethaven.repositories.OrganizationRepository;
import dev.pethaven.repositories.PetRepository;
import dev.pethaven.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    PetMapper petMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    OrganizationMapper organizationMapper;

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
    public PetDTO addPet(Principal user, @RequestBody PetDTO newPetDTO){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Pet tempPet = petMapper.toEntity(newPetDTO);
        Pet newPet = new Pet(null, tempPet.getName(), tempPet.getGender(), tempPet.getTypePet(),
                tempPet.getBirthDay(), tempPet.getBreed(), PetStatus.ACTIVE,
                organizationRepository.findByAuthId(authRepository.findByUsername(user.getName()).get().getId()));
        petRepository.save(newPet);
        return petMapper.toDTO(newPet);
    }
    @DeleteMapping("/organization/pets/{id}")
    public ResponseEntity<?> deletePet(@PathVariable("id") Long id) {
        petRepository.deleteById(id);
        return ResponseEntity.ok().body(new MessageResponse("Питомец удален."));
    }

    @PatchMapping("/organization/pets/{id}/status")
    public ResponseEntity<?> updateStatusPet(@PathVariable("id") Long petId,
                                             @RequestParam(value = "newStatus") String newStatus) {
        Pet updatedpet = petRepository.findById(petId).get();
        updatedpet.setStatus(PetStatus.valueOf(newStatus));
        petRepository.save(updatedpet);
        return ResponseEntity.ok().body(new MessageResponse("Статус изменен."));
    }

    @PutMapping("/organization/pets/{id}")
    public PetDTO updatePet(@PathVariable("id") Long petId, @RequestBody PetDTO newPet) {
        Pet pet = petRepository.findById(petId).get();
        petMapper.updatePet(newPet, pet);
        petRepository.save(pet);
        return petMapper.toDTO(pet);
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
