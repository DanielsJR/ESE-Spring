package nx.ese.dtos.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NxRutValidator.class)
public @interface NxRutValid {

    String message() default "Expected RUT match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}