package dev.pethaven.controllers;

import dev.pethaven.dto.OrganizationDTO;
import dev.pethaven.dto.PetDTO;
import dev.pethaven.dto.UserDTO;
import dev.pethaven.entity.Pet;
import dev.pethaven.enums.PetStatus;
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
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/profile")
public class ProfileeController {
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
    public UserDTO updateUser(@RequestBody UserDTO updatedUser) {
        return userService.updateUser(updatedUser);
    }

    @DeleteMapping("/user")
    public void deleteUser(Principal principal) {
        userService.deleteCurrentUser(principal);
    }

    @GetMapping("/organization")
    public OrganizationDTO getOrganization(Principal principal) {
        return organizationService.getCurrentOrganization(principal);
    }

    @PutMapping("/organization")
    public OrganizationDTO updateOrganization(@RequestBody OrganizationDTO updatedOrganization) {
        return organizationService.updateOrganization(updatedOrganization);
    }

    @DeleteMapping("/organization")
    public void deleteOrganization(Principal principal) {
        organizationService.deleteCurrentOrganization(principal);
    }

    @GetMapping("/organization/pets")
    public List<PetDTO> getAllPets(Principal principal) {
        return petService.getAllPetsCurrentOrganization(principal);
    }

    @PostMapping("/organization/pets")
    public PetDTO addPet(Principal user, @ModelAttribute SavePet newPetInfo) {
        return petService.addPet(user, newPetInfo);
    }

    @PutMapping("/organization/pets/{id}")
    public PetDTO updatePet(@PathVariable("id") Long petId, @ModelAttribute SavePet updatedPet) {
        return petService.updatePet(petId, updatedPet);
    }

    @DeleteMapping("/organization/pets/{id}")
    public void deletePet(@PathVariable("id") Long id) {
        petService.deletePet(id);
    }

    @PatchMapping("/organization/pets/{id}/status")
    public ResponseEntity<?> updateStatusPet(@PathVariable("id") Long petId,
                                             @RequestParam(value = "newStatus") String newStatus) {
        petService.updateStatusPet(petId, newStatus);
        return ResponseEntity.ok().body(new MessageResponse("Status updated"));
    }


    @PostMapping("/organization/pets/{id}/adopt")
    public void adoptPet(@PathVariable("id") @NotNull(message = "Id cannot be null") Long petId,
                         @RequestBody String username) {
        petService.adoptPet(username, petId);
    }

    @GetMapping("/organization/pets/{id}/userRequest")
    public Set<UserDTO> getUsersRequsts(@PathVariable("id") @NotNull(message = "Id cannot be null") Long petId) {
        return petService.getUsersRequsts(petId);
    }

    @DeleteMapping("/organization/pets/{id}/delUserRequest")
    public void deleteRequestUser(@PathVariable("id")
                                  Long petId,
                                  @RequestParam(value = "username")
                                  @Size(min = 4, message = "username must be minimum 4 chars")
                                  String username) {
        petService.deleteRequestUser(petId, username);
    }

}
