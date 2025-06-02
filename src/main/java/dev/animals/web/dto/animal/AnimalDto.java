package dev.animals.web.dto.animal;

import dev.animals.enums.GenderType;
import dev.animals.web.dto.UserDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AnimalDto {

  private Long id;
  private String name;
  private GenderType gender;
  private String type;
  private String birthDay;
  private String breed;
  private String description;
  private String status;
  private List<String> photoRefs;
  private String city;
  private String organizationName;
  private String organizationUsername;
  private UserDto userOwner;
  private Map<String, String> attributes;
  private List<UserDto> adoptionRequestUsers;
}
