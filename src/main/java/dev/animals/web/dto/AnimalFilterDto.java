package dev.animals.web.dto;

import dev.animals.enums.GenderType;
import lombok.Data;

@Data
public class AnimalFilterDto {

  private String name;
  private String type;
  private GenderType gender;
  private String city;
  private String organizationName;
}
