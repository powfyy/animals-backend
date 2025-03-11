package dev.animals.web.dto.animal;

import lombok.Data;

import java.util.List;

@Data
public class AnimalDto {

    private Long id;
    private String name;
    private String gender;
  private String type;
    private String birthDay;
    private String breed;
    private String description;
    private String status;
    private List<String> photoRefs;
    private String city;
  private String organizationName;
  private String organizationUsername;
}
