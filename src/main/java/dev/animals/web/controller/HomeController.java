package dev.animals.web.controller;

import dev.animals.service.OrganizationService;
import dev.animals.service.animal.AnimalService;
import dev.animals.web.dto.FilterFields;
import dev.animals.web.dto.animal.AnimalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animal")
public class HomeController {

  private final AnimalService animalService;
  private final OrganizationService organizationService;

  @PostMapping(value = "/{id}")
  public void requestForAnimal(@PathVariable("id") Long animalId, Principal principal) {
    animalService.requestForAnimal(principal.getName(), animalId);
  }

  @PostMapping
  public Page<AnimalDto> getFilteredPets(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "15") int size,
                                         @RequestBody FilterFields filterFields) {
    return animalService.getAll(page, size, filterFields);
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
