package nx.ese.dtos.validators;

import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.RetentionPolicy;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NxListNotEmptyValidator.class)
public @interface NxListNotEmpty {
    String message() default "Expected not empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
