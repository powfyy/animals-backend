package dev.animals.annotation.notblankelements;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotBlankElementsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankElements {

  String message() default "Коллекция не может содержать пустые строки";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
