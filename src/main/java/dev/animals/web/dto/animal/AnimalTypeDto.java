package dev.animals.web.dto.animal;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

@Data
public class AnimalTypeDto implements AnimalTypeDtoValidator {

  @NotBlank(message = "{animal.type.name.notblank.error}")
  private String name;
  @NotNull(message = "{animal.type.priority.notnull.error}")
  private Integer priority;

  @NotEmpty(message = "{animal.type.attributes.notempty.error}")
  private Map<String, Set<String>> attributes;
}
