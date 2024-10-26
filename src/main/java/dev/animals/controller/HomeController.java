package dev.animals.controller;

import dev.animals.dto.AnimalDto;
import dev.animals.dto.FilterFields;
import dev.animals.dto.OrganizationDtoCityName;
import dev.animals.services.AnimalService;
import dev.animals.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

  private final AnimalService animalService;
  private final OrganizationService organizationService;

  @PostMapping(value = "/pets/{id}")
  public void requestForPet(@PathVariable("id") Long animalId, Principal principal) {
    animalService.requestForAnimal(principal.getName(), animalId);
  }

  @PostMapping(value = "/pets")
  public Page<AnimalDto> getFilteredPets(
    @RequestParam(required = false, defaultValue = "0") int page,
    @RequestParam(required = false, defaultValue = "15") int size,
    @RequestBody FilterFields filterFields) {
    return animalService.getAll(page, size, filterFields);
  }

  @GetMapping(value = "/organizations")
  public List<OrganizationDtoCityName> getAllOrganizations() {
    return organizationService.getCityAndName();
  }

  @GetMapping(value = "/pets/{id}")
  public AnimalDto getPet(@PathVariable("id") Long animalId) {
    return animalService.getById(animalId);
  }

  @GetMapping(value = "/pets/{id}/request")
  public Map<String, Boolean> checkRequest(@PathVariable("id") Long animalId, Principal principal) {
    return animalService.checkRequest(principal.getName(), animalId);
  }
}
