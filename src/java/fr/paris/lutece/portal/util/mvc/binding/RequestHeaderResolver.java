package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestHeader;
import fr.paris.lutece.portal.util.mvc.utils.BindingUtils;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Resolver for method parameters annotated with {@link RequestHeader}.
 * Extracts values from HTTP request headers.
 */
@ApplicationScoped
public class RequestHeaderResolver extends AbstractParameterResolver {

private static final String ERROR_REQUIRED_HEADER = "The header '%s' is required but not provided";
   
	/**
	 * {@inheritDoc}
	 */
    @Override
    public boolean supports(Parameter parameter) {
        return parameter.isAnnotationPresent(RequestHeader.class);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ResolverResult<?> resolve(HttpServletRequest request, Parameter parameter) {
        if (_logger.isDebugEnabled()) {
            _logger.debug(LOG_RESOLVING_PARAMETER, parameter.getName(), parameter.getType().getName());
        }

        RequestHeader annotation = parameter.getAnnotation(RequestHeader.class);
        String headerName = BindingUtils.getEffectiveParameterName( parameter );
        Class<?> targetType = parameter.getType();
        
        // Extract header values
        String[] rawValues = extractHeaderValues(request, headerName);
        
        rawValues = validateAndPrepareValues( rawValues, headerName, annotation, targetType);
        if ( rawValues == null ) {
            return getDefaultValue(targetType);
        }
        
       
        //try {
            return convertToTargetType(parameter, rawValues, headerName, targetType);
     /*   } catch (final Throwable t) {
            _logger.error("Error resolving header '{}': {}", headerName, t.getMessage());
            return handleError(targetType, headerName, rawValues[0], t);
        }*/   
    }

    /**
     * Extracts header values from the request.
     */
    private String[] extractHeaderValues(HttpServletRequest request, String headerName) {
        Enumeration<String> headers = request.getHeaders(headerName);
        if (headers == null || !headers.hasMoreElements()) {
            return null;
        }
        List<String> values = Collections.list(headers);
        return values.toArray(new String[0]);
    }
    
    /**
     * Validates and prepares header values.
     */
    private String[] validateAndPrepareValues(String[] rawValues, String headerName, 
                                          RequestHeader annotation, Class<?> targetType) {
        boolean noValue = (rawValues == null || rawValues.length == 0 ||
                          (rawValues.length == 1 && rawValues[0].isEmpty()));

        if (noValue) {
            _logger.debug(LOG_NO_VALUES, headerName);

            // Check if header is required
            if (annotation.required() && annotation.defaultValue().equals(MVCUtils.DEFAULT_NONE)) {
                _logger.error(LOG_REQUIRED_MISSING, headerName, targetType.getName());
                throw new MissingRequestValueException(String.format(ERROR_REQUIRED_HEADER, headerName), headerName, targetType.getName());
            }
            // Use default value if specified
            if (!annotation.defaultValue().equals(MVCUtils.DEFAULT_NONE)) {
                _logger.debug(LOG_DEFAULT_VALUE, annotation.defaultValue(), headerName);
                return new String[]{ annotation.defaultValue() };
            }

            return null;
        }

        _logger.debug(LOG_FOUND_VALUES, rawValues.length, headerName, String.join(", ", rawValues));
        return rawValues;
    }
    
    /**
     * Converts raw values to the target parameter type.
     */
    private <T> ResolverResult<T> convertToTargetType(Parameter parameter, String[] rawValues, 
            String headerName, Class<T> targetType) {
		if (targetType.isArray()) {
		_logger.debug(LOG_ARRAY_PARAM, headerName, targetType.getComponentType().getName());
		return resolveArrayParameter(parameter, rawValues, headerName);
		}
		
		if (Collection.class.isAssignableFrom(targetType)) {
		_logger.debug(LOG_COLLECTION_PARAM, headerName, targetType.getName());
		return resolveCollectionParameter(parameter, rawValues, headerName);
		}
		
		// Maps are not typically used with headers, but we could implement if needed
		
		_logger.debug(LOG_SINGLE_VALUE, headerName);
		return convertValue(rawValues[0], targetType, headerName);
	}
}
