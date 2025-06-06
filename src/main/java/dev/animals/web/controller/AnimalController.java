package dev.animals.web.controller;

import dev.animals.service.animal.AnimalService;
import dev.animals.service.animal.AnimalTypeService;
import dev.animals.web.dto.animal.AnimalDto;
import dev.animals.web.dto.animal.AnimalFilterDto;
import dev.animals.web.dto.animal.AnimalSaveDto;
import dev.animals.web.dto.animal.AnimalTypeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animal")
@Tag(name = "AnimalController", description = "API для работы с животными")
public class AnimalController {

  private final AnimalService animalService;
  private final AnimalTypeService animalTypeService;

  @GetMapping
  @Operation(summary = "Получение животных")
  public Page<AnimalDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = "15") int size) {
    return animalService.getAll(page, size);
  }

  @GetMapping("/organization")
  @Operation(summary = "Получение животных организации")
  public List<AnimalDto> getAllByOrg(Principal principal) {
    return animalService.getAllByOrganizationUsername(principal.getName());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получение животного по id")
  public AnimalDto getById(@PathVariable Long id) {
    return animalService.getById(id);
  }

  @PostMapping("/filter")
  @Operation(summary = "Получение животных с фильтрацией")
  public Page<AnimalDto> getFiltered(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "15") int size,
                                     @RequestBody AnimalFilterDto filterDto) {
    return animalService.getAllFiltered(page, size, filterDto);
  }

  @PostMapping
  @Operation(summary = "Создание животного")
  public AnimalDto create(@RequestBody @Valid AnimalSaveDto dto) {
    return animalService.create(dto);
  }

  @PutMapping
  @Operation(summary = "Обновление животного")
  public AnimalDto update(@RequestBody @Valid AnimalSaveDto dto) {
    return animalService.update(dto);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удаление животного")
  public void delete(@PathVariable Long id) {
    animalService.delete(id);
  }

  @PostMapping("/photo/{id}")
  @Operation(summary = "Сохранение фотографии")
  public void savePhoto(@PathVariable Long id, MultipartFile file) {
    animalService.savePhoto(id, file);
  }

  @DeleteMapping("/photo/{id}")
  @Operation(summary = "Удаление фотографии")
  public void deletePhoto(@PathVariable Long id, @RequestParam String photoRef) {
    animalService.removePhoto(id, photoRef);
  }

  @PostMapping("/adoption-request/{animalId}")
  @Operation(summary = "Создание заявки на животное")
  public void createRequest(@PathVariable Long animalId,  Principal principal) {
    animalService.createAdoptionRequest(principal.getName(), animalId);
  }

  @GetMapping("/type")
  @Operation(summary = "Получение видов животных")
  public Page<AnimalTypeDto> getAllTypes(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
    return animalTypeService.getAll(page, size);
  }

  @PostMapping("/type")
  @Operation(summary = "Сохранение вида животного")
  public void saveType(@RequestBody @Valid AnimalTypeDto dto) {
    animalTypeService.save(dto);
  }

  @PutMapping("/type/priority")
  @Operation(summary = "Обновление приоритетов видов животных")
  public void updateTypePriorities(@RequestBody @Valid List<AnimalTypeDto> dtos) {
    animalTypeService.updatePriorities(dtos);
  }

  @DeleteMapping("/type/{name}")
  @Operation(summary = "Удаление вида животного")
  public void deleteType(@PathVariable String name) {
    animalTypeService.delete(name);
  }
}
