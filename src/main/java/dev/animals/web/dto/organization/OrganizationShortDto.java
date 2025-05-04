package dev.animals.web.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "OrganizationShortDto", description = "Объект с основной информацией об организации")
public class OrganizationShortDto {

  private String username;
  private String name;
  private String city;
}
