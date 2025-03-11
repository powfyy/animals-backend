package dev.animals.web.controller;

import dev.animals.service.OrganizationService;
import dev.animals.service.animal.AnimalService;
import dev.animals.web.dto.OrganizationDto;
import dev.animals.web.dto.UserDto;
import dev.animals.web.dto.animal.AnimalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor //todo разнести эндпоинты по соответствующим контроллерам
@RequestMapping("/profile")
public class ProfileeController {

  private final AnimalService animalService;
  private final OrganizationService organizationService;

  @GetMapping("/organization")
  public OrganizationDto getOrganization(Principal principal) {
    return organizationService.getByUsername(principal.getName());
  }

  @PutMapping("/organization")
  public OrganizationDto updateOrganization(@RequestBody @Valid OrganizationDto updatedOrganization) {
    return organizationService.update(updatedOrganization);
  }

  @DeleteMapping("/organization")
  public void deleteOrganization(Principal principal) {
    organizationService.deleteByUsername(principal.getName());
  }

  @GetMapping("/organization/pets")
  public List<AnimalDto> getAllPets(Principal principal) {
    return animalService.getAllByOrganizationUsername(principal.getName());
  }

//  @PostMapping("/organization/pets")
//  public AnimalDto addPet(Principal principal, @Valid AnimalSaveDto newPetInfo) {
//    return animalService.create(principal.getName(), newPetInfo);
//  }
//
//  @PutMapping("/organization/pets/{id}")
//  public AnimalDto updatePet(@PathVariable("id") Long id, @Valid AnimalSaveDto dto) {
//    return animalService.update(id, dto);
//  }
//
//  @DeleteMapping("/organization/pets/{id}")
//  public void deletePet(@PathVariable("id") Long id) {
//    animalService.delete(id);
//  }
//
//  @PatchMapping("/organization/pets/{id}/status")
//  public ResponseEntity<?> updateStatusPet(@PathVariable("id") Long animalId,
//                                           @RequestParam(value = "newStatus") String newStatus) {
//    animalService.updateStatusPet(animalId, newStatus);
//    return ResponseEntity.ok().body(new MessageResponse("Status updated"));
//  }
//
//
//  @PostMapping("/organization/pets/{id}/adopt")
//  public void adoptPet(@PathVariable("id") Long animalId, @RequestBody String username) { //todo поправить username
//    animalService.adoptPet(username, animalId);
//  }

  @GetMapping("/organization/pets/{id}/userRequest")
  public Set<UserDto> getUserRequests(@PathVariable("id") Long animalId) {
    return animalService.getUserRequests(animalId);
  }

  @DeleteMapping("/organization/pets/{id}/delUserRequest")
  public void deleteRequestUser(@PathVariable("id") Long animalId,
                                @RequestParam(value = "username") String username) {
    animalService.deleteRequestUser(animalId, username);
  }

}
