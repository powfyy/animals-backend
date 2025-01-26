package dev.animals.web.controller;

import dev.animals.service.OrganizationService;
import dev.animals.web.dto.OrganizationCityNameDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organization")
@Tag(name = "OrganizationController", description = "API для работы с организациями")
public class OrganizationController {

  private final OrganizationService service;

  @GetMapping("/city")
  public List<OrganizationCityNameDto> getCityAndName() {
    return service.getCityAndName();
  }
}
