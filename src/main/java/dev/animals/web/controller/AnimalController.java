package dev.animals.web.controller;

import dev.animals.service.animal.AnimalService;
import dev.animals.service.animal.AnimalTypeService;
import dev.animals.web.dto.animal.AnimalTypeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animal")
@Tag(name = "AnimalController", description = "API для работы с животными")
public class AnimalController {

  private final AnimalService animalService;
  private final AnimalTypeService animalTypeService;

  @GetMapping("/type")
  @Operation(summary = "Получение видов животных")
  public Page<AnimalTypeDto> getAllTypes(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
    return animalTypeService.getAll(page, size);
  }

  @PostMapping("/type")
  @Operation(summary = "Сохранение вида животного")
  public void saveType(@RequestBody AnimalTypeDto dto) {
    animalTypeService.save(dto);
  }

  @PostMapping("/type/{name}")
  @Operation(summary = "Сохранение вида животного")
  public void deleteType(@PathVariable String name) {
    animalTypeService.delete(name);
  }
}
