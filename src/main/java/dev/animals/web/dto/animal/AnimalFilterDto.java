package dev.animals.web.dto.animal;

import lombok.Data;

@Data
public class AnimalFilterDto {

  private String name;
  private String type;
  private String gender;
  private String city;
  private String organizationName;
}
