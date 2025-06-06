package dev.animals.web.controller;

import dev.animals.service.AttributeService;
import dev.animals.web.dto.AttributeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attribute")
@Tag(name = "AttributeController", description = "API для работы с атрибутами")
public class AttributeController {

  private final AttributeService service;

  @GetMapping
  @Operation(summary = "Получение всех атрибутов")
  public Page<AttributeDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "15") int size) {
    return service.getAll(page, size);
  }

  @GetMapping("/{name}")
  @Operation(summary = "Получение атрибута по названию")
  public AttributeDto getByName(@PathVariable String name) {
    return service.getByName(name);
  }

  @GetMapping("/type/{typeName}")
  @Operation(summary = "Получение атрибута по названию вида животного")
  public List<AttributeDto> getByAnimalTypeName(@PathVariable String typeName) {
    return service.getByAnimalTypeName(typeName);
  }

  @PostMapping
  @Operation(summary = "Сохранение атрибута")
  public void save(@RequestBody @Valid AttributeDto dto) {
    service.save(dto);
  }

  @PutMapping("/priority")
  @Operation(summary = "Обновление приоритетов атрибутов")
  public void save(@RequestBody @Valid List<AttributeDto> dto) {
    service.updatePriorities(dto);
  }

  @DeleteMapping("/{name}")
  @Operation(summary = "Удаление атрибута по названию")
  public void delete(@PathVariable String name) {
    service.delete(name);
  }
}
