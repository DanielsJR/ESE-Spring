package nx.ese.dtos.validators;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NxListNotEmptyValidator implements ConstraintValidator<NxListNotEmpty, List<?>> {

    @Override
    public void initialize(NxListNotEmpty constraint) {
        // Empty, not operation
    }

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        return list != null && !list.isEmpty();
    }

}
