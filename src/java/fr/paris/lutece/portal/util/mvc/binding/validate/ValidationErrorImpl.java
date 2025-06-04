package fr.paris.lutece.portal.util.mvc.binding.validate;

import jakarta.validation.ConstraintViolation;
/**
 * Represents a validation error that occurs during the validation process in the MVC framework.
 * This class encapsulates details about the error, including the parameter name,
 * the constraint violation, and a human-readable error message.
 */
public class ValidationErrorImpl implements ValidationError {

	private final ConstraintViolation<?> violation;
    private final String param;
    private final String message;

    public ValidationErrorImpl(ConstraintViolation<?> violation, String param, String message) {
        this.violation = violation;
        this.param = param;
        this.message = message;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getParamName() {
        return param;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConstraintViolation<?> getViolation() {
        return violation;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return message;
    }

}
