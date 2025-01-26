package dev.animals.web.dto;

import dev.animals.annotation.notblankelements.NotBlankElements;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AttributeDto {

  @NotBlank(message = "{attribute.name.notblank.error}")
  private String name;

  @NotNull(message = "{attribute.priority.notnull.error}")
  @Min(value = 0, message = "{attribute.priority.min.error}")
  private Integer priority;

  @NotEmpty(message = "{attribute.values.notempty.error}")
  @NotNull(message = "{attribute.values.notnull.error}")
  @NotBlankElements(message = "{attribute.values.notblankelements.error}")
  private Set<String> values;
}
