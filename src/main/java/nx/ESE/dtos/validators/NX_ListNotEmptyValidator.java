package nx.ESE.dtos.validators;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NX_ListNotEmptyValidator implements ConstraintValidator<NX_ListNotEmpty, List<?>> {

    @Override
    public void initialize(NX_ListNotEmpty constraint) {
        // Empty, not operation
    }

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        return list != null && !list.isEmpty();
    }

}
