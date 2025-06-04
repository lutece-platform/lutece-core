package fr.paris.lutece.portal.util.mvc.binding.validate;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import fr.paris.lutece.portal.util.mvc.commons.annotations.CookieValue;
import fr.paris.lutece.portal.util.mvc.commons.annotations.ModelAttribute;
import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestHeader;
import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestParam;
import jakarta.validation.ConstraintViolation;

/**
 * Metadata class that holds information about a constraint violation and its associated annotations.
 * This class provides methods to retrieve specific annotations and parameter names from the violation.
 */
public class ConstraintViolationMetadata {

	private final ConstraintViolation<?> violation;
    private final Annotation[] annotations;

    /**
	 * Constructs a ConstraintViolationMetadata instance with the given violation and annotations.
	 *
	 * @param violation the constraint violation
	 * @param annotations the annotations associated with the violation
	 */
    public ConstraintViolationMetadata(ConstraintViolation<?> violation, Annotation[] annotations) {
        this.violation = Objects.requireNonNull(violation, "violations");
        this.annotations = Objects.requireNonNull(annotations, "annotations");
    }
    /**
     *  Return an Optional containing the first annotation of the specified type, if present
     * @param type the type of annotation to retrieve
     * @return an Optional containing the first annotation of the specified type, if present
     */
    public Optional<Annotation> getAnnotation(Class<Annotation> type) {
        return Arrays.stream(annotations).filter(a -> a.annotationType().equals(type)).findFirst();
    }

    /**
	 * Checks if the metadata contains an annotation of the specified type.
	 *
	 * @param type the type of annotation to check for
	 * @return true if the annotation is present, false otherwise
	 */
    public boolean hasAnnotation(Class<? extends Annotation> type) {
        return Arrays.stream(annotations).anyMatch(a -> a.annotationType().equals(type));
    }
    
    /** * Retrieves the parameter name associated with the violation.
	 * It checks for specific annotations like RequestParam, RequestHeader, ModelAttribute, and CookieValue,
	 * and returns the appropriate value based on the annotation's value or the property path of the violation.
	 *
	 * @return an Optional containing the parameter name
	 */
    public Optional<String> getParamName() {
        for (Annotation annotation : annotations) {
            if (annotation instanceof RequestParam) {
            	 RequestParam requestParam = (RequestParam) annotation;
                 return Optional.of(!requestParam.value().isEmpty() ? requestParam.value() : violation.getPropertyPath().toString());   
            }
            if (annotation instanceof RequestHeader) {
            	RequestHeader requestHeader = (RequestHeader) annotation;
                return Optional.of(!requestHeader.value().isEmpty() ? requestHeader.value() : violation.getPropertyPath().toString());          
            }
            if (annotation instanceof ModelAttribute) {
                return Optional.of(violation.getPropertyPath().toString());
            }
            if (annotation instanceof CookieValue) {
            	CookieValue cookieValue = (CookieValue) annotation;
                return Optional.of(!cookieValue.value().isEmpty() ? cookieValue.value() : violation.getPropertyPath().toString());             
            }
        }
        return  Optional.of(violation.getPropertyPath().toString());
    }
}
