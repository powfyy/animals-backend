package dev.animals.web.controller;

import dev.animals.service.JwtService;
import dev.animals.service.OrganizationService;
import dev.animals.web.dto.organization.OrganizationCityNameDto;
import dev.animals.web.dto.organization.OrganizationDto;
import dev.animals.web.dto.organization.OrganizationShortDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organization")
@Tag(name = "OrganizationController", description = "API для работы с организациями")
public class OrganizationController {

  private final OrganizationService service;
  private final JwtService jwtService;

  @GetMapping
  @Operation(summary = "Получение всех организаций")
  public List<OrganizationShortDto> getAll() {
    return service.getAll();
  }

  @GetMapping("/{username}")
  @Operation(summary = "Получение организации по username")
  public OrganizationDto getByUsername(@PathVariable("username") String username) {
    return service.getByUsername(username);
  }

  @PutMapping
  @Operation(summary = "Обновление организации")
  public OrganizationDto update(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                @RequestBody OrganizationDto organizationDto) {
    organizationDto.setUsername(jwtService.getUserNameFromJwtToken(authorization));
    return service.update(organizationDto);
  }

  @DeleteMapping
  @Operation(summary = "Удаление текущей организации")
  public void deleteCurrent(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
    service.deleteByUsername(jwtService.getUserNameFromJwtToken(authorization));
  }

  @Deprecated
  @GetMapping("/city")
  public List<OrganizationCityNameDto> getCityAndName() {
    return service.getCityAndName();
  }
}
