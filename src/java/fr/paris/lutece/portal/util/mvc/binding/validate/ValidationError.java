package fr.paris.lutece.portal.util.mvc.binding.validate;

import fr.paris.lutece.portal.util.mvc.binding.ParamError;
import jakarta.validation.ConstraintViolation;
/**
 * Represents an error that occurs during the validation process in the MVC lutece framework.
 * This interface extends {@link ParamError} to include details about the specific
 * validation violation detected for a parameter.
 */
public interface ValidationError extends ParamError {

	/**
     * The underlying {@link ConstraintViolation} detected for the parameter.
     *
     * @return The violation detected for the parameter
     */
    ConstraintViolation<?> getViolation();
}
