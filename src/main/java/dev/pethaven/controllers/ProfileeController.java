package dev.pethaven.controllers;

import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.PetDTO;
import dev.pethaven.dto.UserDTO;
import dev.pethaven.entity.Pet;
import dev.pethaven.entity.PetStatus;
import dev.pethaven.exception.NotFoundException;
import dev.pethaven.mappers.UserMapper;
import dev.pethaven.dto.MessageResponse;
import dev.pethaven.dto.SavePet;
import dev.pethaven.repositories.PetRepository;
import dev.pethaven.services.OrganizationService;
import dev.pethaven.services.PetService;
import dev.pethaven.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/profile")
public class ProfileeController {
    @Autowired
    PetRepository petRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PetService petService;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    UserService userService;

    @GetMapping("/user")
    public UserDTO getCurrentUser(Principal principal) {
        return userService.getCurrentUser(principal);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO updatedUser) {
        userService.updateUser(updatedUser);
        return ResponseEntity.ok().body(new MessageResponse("User updated"));
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(Principal principal) {
        userService.deleteCurrentUser(principal);
        return ResponseEntity.ok().body(new MessageResponse("User deleted"));
    }

    @GetMapping("/organization")
    public OrganizationDTO getOrganization(Principal principal) {
        return organizationService.getCurrentOrganization(principal);
    }

    @PutMapping("/organization")
    public ResponseEntity<?> updateOrganization(@RequestBody OrganizationDTO updatedOrganization) {
        organizationService.updateOrganization(updatedOrganization);
        return ResponseEntity.ok().body(new MessageResponse("Organization updated"));
    }

    @DeleteMapping("/organization")
    public ResponseEntity<?> deleteOrganization(Principal principal) {
        organizationService.deleteCurrentOrganization(principal);
        return ResponseEntity.ok().body(new MessageResponse("Organization deleted"));
    }

    @GetMapping("/organization/pets")
    public List<PetDTO> getAllPets(Principal principal) {
        return petService.getAllPetsCurrentOrganization(principal);
    }

    @PostMapping("/organization/pets")
    public ResponseEntity<?> addPet(Principal user, @ModelAttribute SavePet newPetInfo) {
        petService.addPet(user, newPetInfo);
        return ResponseEntity.ok().body(new MessageResponse("Pet created"));
    }

    @PutMapping("/organization/pets/{id}")
    public ResponseEntity<?> updatePet(@PathVariable("id") Long petId, @ModelAttribute SavePet updatedPet) {
        petService.updatePet(petId, updatedPet);
        return ResponseEntity.ok().body(new MessageResponse("Pet updated"));
    }

    @DeleteMapping("/organization/pets/{id}")
    public ResponseEntity<?> deletePet(@PathVariable("id") Long id) {
        petService.deletePet(id);
        return ResponseEntity.ok().body(new MessageResponse("Pet deleted."));
    }

    @PatchMapping("/organization/pets/{id}/status")
    public ResponseEntity<?> updateStatusPet(@PathVariable("id") Long petId,
                                             @RequestParam(value = "newStatus") String newStatus) {
        petService.updateStatusPet(petId, newStatus);
        return ResponseEntity.ok().body(new MessageResponse("Status updated"));
    }


    @PostMapping("/organization/pets/{id}/adopt")
    public ResponseEntity<?> adoptPet(@PathVariable("id") @NotNull(message = "Id cannot be null") Long petId,
                                      @RequestBody String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException("Pet is not found"));
        if (pet.getStatus() == PetStatus.FREEZE) {
            petService.adoptPet(username, pet);
            return ResponseEntity.ok(new MessageResponse("Pet adopted"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Incorrect pet status. Requires 'FREEZE'."));
    }

    @GetMapping("/organization/pets/{id}/userRequest")
    public Set<UserDTO> getUsersRequsts(@PathVariable("id") @NotNull(message = "Id cannot be null") Long petId) {
        return userMapper.toDtoSet(petRepository
                .findById(petId).orElseThrow(() -> new NotFoundException("Pet is not found"))
                .getUserSet());
    }

    @DeleteMapping("/organization/pets/{id}/delUserRequest")
    public ResponseEntity<?> deleteRequestUser(@PathVariable("id") Long petId,
                                               @RequestParam(value = "username") @Size(min = 4, message = "username must be minimum 4 chars")
                                               String username) {
        petService.deleteRequestUser(petId, username);
        return ResponseEntity.ok(new MessageResponse("Request deleted."));
    }
}
