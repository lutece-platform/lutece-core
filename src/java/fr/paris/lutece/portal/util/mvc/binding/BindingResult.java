package fr.paris.lutece.portal.util.mvc.binding;

import java.util.List;
import java.util.Set;

import fr.paris.lutece.portal.util.mvc.binding.validate.ValidationError;

/**
 * Represents the result of a binding operation in the Lutece MVC framework.
 * This interface provides methods to check for errors, retrieve error messages,
 * and access detailed information about binding and validation errors.
 */
public interface BindingResult {

	   /**
     * Returns <code>true</code> if there is at least one parameter error.
     *
     * @return <code>true</code> if there is at least one parameter error
     */
    boolean isFailed();

    /**
     * Returns an immutable list of all messages representing both binding and
     * validation errors. This method is a shortcut for:
     *
     * <pre>
     * getAllErrors().stream().map(ParamError::getMessage).collect(Collectors.toList())
     * </pre>
     *
     * @return A list of human-readable messages
     */
    List<String> getAllMessages();

    /**
     * Returns an immutable set of all binding and validation errors.
     *
     * @return All binding and validation errors.
     */
    Set<ParamError> getAllErrors();
   
    /**
     * Returns an immutable set of all binding errors.
     *
     * @return All binding errors.
     */
    Set<BindingError> getBindingErrors();
    
    /**
     * Returns an immutable set of all validation errors.
     *
     * @return All validation errors.
     */
	Set<ValidationError> getValidationErrors();

    /**
     * Returns an immutable set of all binding and validation errors for
     * a specific parameter.
     *
     * @param param parameter name
     * @return All binding and validation errors for the parameter.
     */
    Set<ParamError> getErrors(String param);
}
