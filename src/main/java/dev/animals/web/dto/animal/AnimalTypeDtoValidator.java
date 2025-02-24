package dev.animals.web.dto.animal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public interface AnimalTypeDtoValidator {

  Map<String, Set<String>> getAttributes();

  @JsonIgnore
  @AssertTrue(message = "{animal.type.attributes.value.notblank.error}")
  default boolean isAttributesValid() {
    return Objects.isNull(getAttributes()) ||
      getAttributes().isEmpty() ||
      getAttributes().entrySet().stream()
        .anyMatch(entry -> entry.getValue().isEmpty() || entry.getValue().stream()
          .anyMatch(StringUtils::isBlank));
  }
}
