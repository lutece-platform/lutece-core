package fr.paris.lutece.portal.util.mvc.binding.validate;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.util.mvc.binding.BindingResultImpl;
import fr.paris.lutece.portal.util.mvc.commons.annotations.ModelAttribute;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Validated;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.cdi.mvc.MvcInternal;
import fr.paris.lutece.util.beanvalidation.DefaultValidationErrorConfig;
import fr.paris.lutece.util.beanvalidation.ValidationErrorUtil;
import fr.paris.lutece.util.beanvalidation.ValidationErrorConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;

/**
 * Service responsible for validating method parameters and return values
 * within the Lutece MVC framework using Jakarta Bean Validation (JSR-380).
 * <p>
 * This service integrates with the MVC binding mechanism to:
 *  <ul>
 *   <li>If they are also annotated with {@link jakarta.validation.Valid}, standard
 *   Jakarta validation constraints are applied.</li>
 *   <li>If they are annotated with {@link Validated}, validation is performed using
 *   the specified validation groups (group-based validation).</li>
 *  </ul>
 *   In addition, method-level parameter validation is performed using an
 *   {@link ExecutableValidator}.
 *   The service also formats localized error messages for user-friendly feedback.
 * </p>
 * 
 * <p>
 * It uses {@link jakarta.validation.Validator} and {@link jakarta.validation.executable.ExecutableValidator}
 * for runtime validation of method arguments and return values.
 * </p>
 */
@ApplicationScoped
public class ValidationService {
    private static final Logger _logger = MVCUtils.getLogger( );
    @Inject
    private BindingResultImpl bindingResult;
    @Inject
    @MvcInternal
    private ValidatorFactory validatorFactory;

    /**
     * Default constructor.
     */
    public ValidationService() {        
    }

    /**
     * Validates the parameters of a controller method using Jakarta Bean Validation.
     * <p>
     * This method performs validation on parameters annotated with {@link ModelAttribute}.
     * For those parameters:
     * <ul>
     *   <li>If they are also annotated with {@link jakarta.validation.Valid}, standard
     *   Jakarta validation constraints are applied.</li>
     *   <li>If they are annotated with {@link Validated}, validation is performed using
     *   the specified validation groups (group-based validation).</li>
     * </ul>
     * In addition, method-level parameter validation is performed using an
     * {@link ExecutableValidator}.
     * </p>
     *
     * @param resource   The controller/resource instance on which the method is invoked.
     * @param method     The method whose parameters are being validated.
     * @param parameters The parameter values passed to the method.
     */
    public void validateParameters(Object resource, Method method, Object[] parameters) {
    	Validator validator = validatorFactory.getValidator();
    	ExecutableValidator executableValidator = validator.forExecutables();
        Parameter[] params = method.getParameters();
        Set<ConstraintViolation<Object>> allViolations = new HashSet<>();        
        for (int i = 0; i < params.length && i < parameters.length; i++) {
            Parameter param = params[i];
            Object paramValue = parameters[i];
            
            // First, check if the parameter is annotated with @ModelAttribute
            if (!param.isAnnotationPresent(ModelAttribute.class)) {
                continue; // Skip parameters that are not annotated with @ModelAttribute
            }
            
            if (paramValue == null) continue;
            
         // Check for your @Validated annotation on @ModelAttribute parameters
            Validated validGroup = param.getAnnotation(Validated.class);
            if (validGroup != null) {
            	// Validate using the specified groups (no need for @Valid)               
            	Set<ConstraintViolation<Object>> violations = 
                    validator.validate(paramValue, validGroup.value());
                allViolations.addAll(violations);
            }
        }
        Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(resource, method, parameters);
    	allViolations.addAll(violations);
 
        processViolations(allViolations);
        
    }
    /**
	 * Validates the return value of a controller method using Jakarta Bean Validation.
	 * <p>
	 * This method performs validation on the return value of a method annotated with
	 * {@link Validated} or {@link jakarta.validation.Valid}. It uses the
	 * {@link ExecutableValidator} to validate the return value against the method's
	 * constraints.
	 * </p>
	 *
	 * @param resource    The controller/resource instance on which the method is invoked.
	 * @param method      The method whose return value is being validated.
	 * @param returnValue The return value of the method to validate.
	 */
    public void validateReturnValue(Object resource, Method method, Object returnValue) {
    	Validator validator = validatorFactory.getValidator();
    	ExecutableValidator executableValidator = validator.forExecutables();
    
    	Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(resource, method, returnValue);
        processViolations(violations);
    }

    /**
	 * Processes the set of constraint violations and updates the binding result.
	 * <p>
	 * This method checks for existing binding errors and ignores validation errors
	 * on parameters that already have binding errors. It formats the error messages
	 * and adds them to the binding result.
	 * </p>
	 *
	 * @param violations The set of constraint violations to process.
	 */
    private void processViolations(Set<ConstraintViolation<Object>> violations) {
    	 if (violations.isEmpty()) return;

    	 _logger.trace("Validation found {} constraint violations...", violations.size());
         Set<ValidationError> validationErrors = new LinkedHashSet<>();
         
         for (ConstraintViolation<Object> violation : violations) {
             ConstraintViolationMetadata metadata = ConstraintViolations.getMetadata(violation);
             String paramName = metadata.getParamName().orElse(null);
             // Ne pas ajouter l'erreur si le paramètre a déjà une erreur de binding
         
             boolean hasBindingError = paramName != null && !paramName.isEmpty() &&
                     bindingResult.getErrors(paramName).size() > 0;

             if (hasBindingError) {
            	 _logger.trace("Ignoring validation error on already invalid parameter: {}", paramName);
                 continue;
             }
             //String message = violation.getMessage();
             String message = getMessage(violation, paramName);
             validationErrors.add(new ValidationErrorImpl(violation,paramName, message ));
         }
         // update BindingResult
         if (!validationErrors.isEmpty()) {
        	 _logger.trace("Adding {} validation errors to binding result", validationErrors.size());
             bindingResult.addValidationErrors(validationErrors);
         }
    }

    /**
	 * Formats the error message for a constraint violation.
	 * <p>
	 * This method retrieves the message from the constraint violation and formats it
	 * with additional context information such as field name, values, and parameter name.
	 * </p>
	 *
	 * @param constraintViolation The constraint violation to format.
	 * @param paramName           The name of the parameter associated with the violation.
	 * @return The formatted error message.
	 */
    private String getMessage( ConstraintViolation<Object> constraintViolation, String paramName )
    {
    	ValidationErrorConfig config = new DefaultValidationErrorConfig( );

        String strMessage = constraintViolation.getMessage( );
        String strValue1 = ValidationErrorUtil.getValue1( constraintViolation, config );
        String strValue2 = ValidationErrorUtil.getValue2( constraintViolation, config );
        String strFieldname = ValidationErrorUtil.getFieldname( constraintViolation, config, LocalVariables.getLocale( ) );

        strMessage = MessageFormat.format( strMessage, strFieldname, strValue1, strValue2, constraintViolation.getInvalidValue( ), paramName );

        return strMessage;
    }

}
