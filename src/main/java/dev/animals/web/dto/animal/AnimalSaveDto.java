package dev.animals.web.dto.animal;

import dev.animals.enums.AnimalStatus;
import dev.animals.enums.GenderType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class AnimalSaveDto implements AnimalValidator {

  private Long id;
  @Size(min = 2, max = 70, message = "{animal.name.size.error}")
  private String name;
  @NotNull(message = "{animal.gender.notnull.error}")
  private GenderType gender;
  @NotNull(message = "{animal.type.notnull.error}")
  @NotBlank(message = "{animal.type.notblank.error}")
  private String type;
  @NotNull(message = "{animal.birthDay.notnull.error}")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDay;
  private String breed;
  private AnimalStatus status;
  private String description;
  @NotNull(message = "{animal.organizationUsername.notnull.error}")
  @NotBlank(message = "{animal.organizationUsername.notblank.error}")
  private String organizationUsername;
  private String userUsername;
  @NotNull(message = "{animal.attributes.notnull.error}")
  @NotEmpty(message = "{animal.attributes.notempty.error}")
  private Map<String, String> attributes;
  @NotNull(message = "{animal.adoptionRequestUserUsernames.notnull.error}")
  private List<String> adoptionRequestUserUsernames;
}
