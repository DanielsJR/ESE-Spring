package nx.ese.dtos.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NX_RutValidator implements ConstraintValidator<NX_RutValid, String> {

    private static final Logger logger = LoggerFactory.getLogger(NX_RutValidator.class);

    @Override
    public boolean isValid(String rut, ConstraintValidatorContext arg1) {
        if (rut != null) {
            boolean validacion = false;
            try {
                rut = rut.toUpperCase();
                rut = rut.replace(".", "");
                rut = rut.replace("-", "");
                int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

                char dv = rut.charAt(rut.length() - 1);

                int m = 0, s = 1;
                for (; rutAux != 0; rutAux /= 10) {
                    s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
                }
                if (dv == (char) (s != 0 ? s + 47 : 75)) {
                    validacion = true;
                }

            } catch (java.lang.NumberFormatException e) {
                logger.error(e.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            return validacion;
        } else {
            return true;
        }
    }

}
