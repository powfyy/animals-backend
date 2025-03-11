package dev.animals.web.dto.animal;

import dev.animals.enums.AnimalStatus;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.util.Objects;

public interface AnimalValidator {

  AnimalStatus getStatus();

  String getUserUsername();

  @AssertTrue(message = "{animal.userUsername.notnull.error}")
  default boolean isUserUsernameValid() {
    return Objects.isNull(getStatus()) || !getStatus().equals(AnimalStatus.ADOPTED) || StringUtils.isNotBlank(getUserUsername());
  }
}
