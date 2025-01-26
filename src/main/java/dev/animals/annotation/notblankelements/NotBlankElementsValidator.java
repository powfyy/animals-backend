package dev.animals.annotation.notblankelements;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Objects;

public class NotBlankElementsValidator implements ConstraintValidator<NotBlankElements, Collection<String>> {


  @Override
  public boolean isValid(Collection<String> collection, ConstraintValidatorContext context) {
    if (Objects.isNull(collection) || collection.isEmpty()) {
      return true;
    }
    return collection.stream().noneMatch(StringUtils::isBlank);
  }
}
