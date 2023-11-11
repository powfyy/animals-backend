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
import dev.pethaven.services.PetService;
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
    PetService petService;
    @Autowired
    MinioService minioService;

    @GetMapping("/user")
    public UserDTO getUserInfo(Principal user) {
        Auth currentAuth = (authRepository.findByUsername(user.getName())).get();
        return userMapper.toDTO(userRepository.findByAuthId(currentAuth.getId()));
    }
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(Principal principal, @RequestBody UserDTO updatedUser){
        User user = userRepository.findByAuthId(authRepository.findByUsername(principal.getName()).get().getId());
        userMapper.updateUser(updatedUser,user);
        userRepository.save(user);
        return ResponseEntity.ok().body(new MessageResponse("User updated"));
    }
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(Principal user){
        userRepository.deleteByAuthId(authRepository.findByUsername(user.getName()).get().getId());
        return ResponseEntity.ok().body(new MessageResponse("User deleted"));
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
        organizationRepository.deleteByAuthId(authRepository.findByUsername(user.getName()).get().getId());
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
        petService.addPet(user, newPetInfo);
        return ResponseEntity.ok().body(new MessageResponse("Pet created"));
    }

    @PutMapping ("/organization/pets/{id}")
    @Transactional
    public ResponseEntity <?> updatePet (@PathVariable("id") Long petId, @ModelAttribute SavePet updatedPet){
       petService.updatePet(petId, updatedPet);
       return ResponseEntity.ok().body(new MessageResponse("Pet updated"));
    }
    @DeleteMapping("/organization/pets/{id}")
    @Transactional
    public ResponseEntity<?> deletePet(@PathVariable("id") Long id) {
        petService.deletePet(id);
        return ResponseEntity.ok().body(new MessageResponse("Pet deleted."));
    }

    @PatchMapping("/organization/pets/{id}/status")
    public ResponseEntity<?> updateStatusPet(@PathVariable("id") Long petId,
                                             @RequestParam(value = "newStatus") String newStatus) {
       petService.updateStatusPet(petId,newStatus);
        return ResponseEntity.ok().body(new MessageResponse("Status updated"));
    }


    @PostMapping("/organization/pets/{id}/adopt")
    public ResponseEntity<?> adoptPet(@PathVariable("id") Long petId, @RequestBody String username) {
        Pet pet = petRepository.findById(petId).get();
        if (pet.getStatus() == PetStatus.FREEZE) {
            petService.adoptPet(username, pet);
            return ResponseEntity.ok(new MessageResponse("Pet adopted"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Incorrect pet status. Requires 'FREEZE'."));
    }

    @GetMapping("/organization/pets/{id}/userRequest")
    public Set<UserDTO> getUserRequsts(@PathVariable("id") Long petId) {
        return userMapper.toDtoSet(petRepository.findById(petId).get().getUserSet());
    }

    @DeleteMapping("/organization/pets/{id}/delUserRequest")
    public ResponseEntity<?> deleteRequestUser(@PathVariable("id") Long petId,
                                               @RequestParam(value = "username") String username) {
        petService.deleteRequestUser(petId, username);
        return ResponseEntity.ok(new MessageResponse("Request deleted."));
    }
}
