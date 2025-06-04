package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestParam;
import fr.paris.lutece.portal.util.mvc.utils.BindingUtils;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;

/**
 * RequestParamResolver is responsible for resolving method parameters annotated with {@link RequestParam}.
 * It supports conversion to primitive types, arrays, collections, and maps from HTTP request parameters.
 */
@ApplicationScoped
public class RequestParamResolver extends AbstractParameterResolver {
	 private static final String ERROR_REQUIRED_PARAM = "The parameter '%s' is required but not provided";
    private static final String MAP_KEY_PREFIX_SEPARATOR = ".";

    
    /**
     * Determines if this resolver supports the given parameter.
     *
     * @param parameter the method parameter
     * @return true if the parameter is annotated with {@link RequestParam}
     */
	@Override
    public boolean supports(Parameter parameter) {
        return parameter.isAnnotationPresent(RequestParam.class);
    }
	/**
     * Resolves the value of the request parameter based on its annotation and type.
     *
     * @param request   the HTTP request
     * @param parameter the method parameter to resolve
     * @return the converted value
     */
    @Override
    public ResolverResult<?> resolve(HttpServletRequest request, Parameter parameter) {
    	 if (_logger.isDebugEnabled()) {
             _logger.debug("Resolving parameter: '{}' of type: '{}'", parameter.getName(), parameter.getType().getName());
         }
    	RequestParam annotation = parameter.getAnnotation(RequestParam.class);
        String paramName = BindingUtils.getEffectiveParameterName(parameter);
        Class<?> targetType = parameter.getType();
        // Handle default or missing values
        String[] rawValues = validateAndPrepareValues(request.getParameterValues(paramName), paramName, annotation, targetType);
        if ( rawValues == null ) {
            return getDefaultValue(targetType);
        }
        return convertToTargetType(request, parameter, rawValues, paramName, targetType);
    }
    
    /**
     * Validates and prepares parameter values.
     */
    private String[] validateAndPrepareValues(String[] rawValues, String paramName, 
                                          RequestParam annotation, Class<?> targetType) {
        boolean noValue = (rawValues == null || rawValues.length == 0 ||
                          (rawValues.length == 1 && rawValues[0].isEmpty()));

        if (noValue) {
            _logger.debug(LOG_NO_VALUES, paramName);

            // Check if parameter is required
            if (annotation.required() && annotation.defaultValue().equals(MVCUtils.DEFAULT_NONE)) {
                _logger.error(LOG_REQUIRED_MISSING, paramName, targetType.getName());
                throw new MissingRequestValueException(String.format(ERROR_REQUIRED_PARAM, paramName), paramName,targetType.getName() );
            }

            // Use default value if specified
            if (!annotation.defaultValue().equals(MVCUtils.DEFAULT_NONE)) {
                _logger.debug(LOG_DEFAULT_VALUE, annotation.defaultValue(), paramName);
                return new String[]{ annotation.defaultValue() };
            }

            return null;
        }
        _logger.debug(LOG_FOUND_VALUES, rawValues.length, paramName, String.join(", ", rawValues));
        return rawValues;
    }
    /**
     * Converts raw values to the target parameter type.
     */
    private <T> ResolverResult<T> convertToTargetType(HttpServletRequest request, Parameter parameter, 
                                      String[] rawValues, String paramName, Class<T> targetType) {
        if (targetType.isArray()) {
            _logger.debug(LOG_ARRAY_PARAM, paramName, targetType.getComponentType().getName());
            return resolveArrayParameter(parameter, rawValues, paramName);
        }

        if (Collection.class.isAssignableFrom(targetType)) {
            _logger.debug(LOG_COLLECTION_PARAM, paramName, targetType.getName());
            return resolveCollectionParameter(parameter, rawValues, paramName);
        }

        if (Map.class.isAssignableFrom(targetType)) {
            _logger.debug(LOG_MAP_PARAM, paramName, targetType.getName());
            return resolveMapParameter(request, parameter, paramName);
        }

        _logger.debug(LOG_SINGLE_VALUE, paramName);
        return convertValue(rawValues[0], targetType, paramName);
    }
    /**
     * Resolves parameters of type Map from the request, using a prefix to extract nested values.
     */
    private <T> ResolverResult<T> resolveMapParameter(HttpServletRequest request, Parameter parameter, String prefix) {
        Map<Object, Object> result = createMapInstance(parameter.getType());
        Class<?>[] keyValueTypes = getMapKeyValueTypes(parameter);
        String actualPrefix = prefix + MAP_KEY_PREFIX_SEPARATOR;
        List<BindingError> errors = new ArrayList<>();

        _logger.debug("Looking for map entries with prefix '{}'", actualPrefix);

        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if (entry.getKey().startsWith(actualPrefix)) {
                processMapEntry(entry, actualPrefix, keyValueTypes, result, errors );
            }
        }

        _logger.debug("Created map with {} entries", result.size());
       
        @SuppressWarnings("unchecked")
		T typedResult = (T) result;
        
        return errors.isEmpty() ? 
        		ResolverResult.success(typedResult) : 
        		ResolverResult.failed(typedResult, errors);
    }
    /**
     * Processes an individual map entry from the request parameters.
     */
    private void processMapEntry(Map.Entry<String, String[]> entry, String actualPrefix,
                              Class<?>[] keyValueTypes, Map<Object, Object> result, List<BindingError> errors) {
        String key = entry.getKey().substring(actualPrefix.length());
        String[] values = entry.getValue();

        if (values != null && values.length > 0) {
            try {
                _logger.debug("Converting map entry key '{}' to type '{}'", key, keyValueTypes[0].getName());
                ResolverResult<?>  convertedKey = convertValue(key, keyValueTypes[0], key);

                _logger.debug("Converting map entry value '{}' to type '{}'",
                           values[0], keyValueTypes[1].getName());
                ResolverResult<?>  convertedValue = convertValue(values[0], keyValueTypes[1], key);
                if (convertedKey.getErrors() == null && convertedValue.getErrors() == null) {
                    result.put(convertedKey.getValue(), convertedValue.getValue( ));
                	_logger.debug(LOG_CONVERT_SUCCESS, convertedKey.getValue());
	                _logger.debug(LOG_CONVERT_SUCCESS, convertedValue.getValue());
	            }else {
	                _logger.warn("Failed to convert map entry key '{}' with value '{}'", key, values[0]);
	                errors.addAll(convertedKey.getErrors());
					errors.addAll(convertedValue.getErrors());
				} 
            } catch (Exception e) {
                _logger.error("Failed to convert map entry key '{}' with value '{}'", key, values[0], e);
            }
        }
    }
}
