package fr.paris.lutece.portal.util.mvc.binding;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestBody;
import fr.paris.lutece.util.json.JsonUtil;
import fr.paris.lutece.util.xml.XmlMarshaller;
import fr.paris.lutece.util.xml.XmlUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Resolver for method parameters annotated with {@link RequestBody}.
 * Extracts and deserializes the HTTP request body into the target object type.
 * 
 * <p>
 * This resolver supports JSON deserialization using Jackson ObjectMapper.
 * It can handle various content types including application/json, application/xml,
 * and plain text depending on the target parameter type.
 * </p>
 */
@ApplicationScoped
public class RequestBodyResolver extends AbstractParameterResolver {

	
	/**
     * {@inheritDoc}
     */
	@Override
	public boolean supports(Parameter parameter) {
        return parameter.isAnnotationPresent(RequestBody.class);	}
	/**
     * {@inheritDoc}
     */
	@Override
	public ResolverResult<?> resolve(HttpServletRequest request, Parameter parameter) {
		
		if (_logger.isDebugEnabled()) {
            _logger.debug(LOG_RESOLVING_PARAMETER, parameter.getName(), parameter.getType().getName());
        }

        RequestBody annotation = parameter.getAnnotation(RequestBody.class);
        Class<?> targetType = parameter.getType();
        String paramName = parameter.getName();

        // Read the request body
        String requestBody = readRequestBody(request);
        
        // Validate the body content
        if (isEmptyBody(requestBody)) {
            if (annotation.required()) {
                _logger.error("Required request body is empty for parameter '{}'", paramName);
                throw new MissingRequestValueException(
                    String.format("Request body is empty but required for parameter '%s'", paramName), 
                    paramName, 
                    targetType.getName()
                );
            }
            _logger.debug("Request body is empty, returning default value for parameter '{}'", paramName);
            return getDefaultValue(targetType);
        }

        // Convert the request body to the target type
        return convertRequestBody(requestBody, targetType, paramName, request);
        
	}

	 /**
     * Reads the entire request body as a string.
     *
     * @param request the HTTP servlet request
     * @return the request body as a string, or null if an error occurs
     */
    private String readRequestBody(HttpServletRequest request) {
        try (BufferedReader reader = request.getReader()) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            _logger.error("Error reading request body: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Error reading request body", e);
        }
    }
    
    /**
     * Checks if the request body is empty or null.
     *
     * @param requestBody the request body string
     * @return true if the body is empty, null, or contains only whitespace
     */
    private boolean isEmptyBody(String requestBody) {
        return requestBody == null || requestBody.trim().isEmpty();
    }

    /**
     * Converts the request body string to the target parameter type.
     *
     * @param requestBody the request body as string
     * @param targetType the target type to convert to
     * @param paramName the parameter name for error reporting
     * @param request the HTTP request for content type detection
     * @return the conversion result
     */
    private <T> ResolverResult<T> convertRequestBody(String requestBody, Class<T> targetType, 
                                                   String paramName, HttpServletRequest request) {
        try {
            // Handle String type directly
            if (targetType == String.class) {
                @SuppressWarnings("unchecked")
                T result = (T) requestBody;
                _logger.debug("Returning request body as String for parameter '{}'", paramName);
                return ResolverResult.success(result);
            }

            // Handle byte array
            if (targetType == byte[].class) {
                @SuppressWarnings("unchecked")
                T result = (T) requestBody.getBytes(StandardCharsets.UTF_8);
                _logger.debug("Returning request body as byte array for parameter '{}'", paramName);
                return ResolverResult.success(result);
            }

            // For other types, attempt JSON deserialization
            String contentType = request.getContentType();
            if (contentType != null && contentType.toLowerCase().contains(JsonUtil.CONTENT_TYPE_JSON)) {
                _logger.debug("Deserializing JSON request body to type '{}' for parameter '{}'", 
                            targetType.getName(), paramName);
                T result = JsonUtil.deserialize(requestBody, targetType);
                return ResolverResult.success(result);
            }
            else if (contentType != null && contentType.toLowerCase().contains(XmlUtil.CONTENT_TYPE_XML)) {
                _logger.debug("Deserializing XML request body to type '{}' for parameter '{}'", 
                            targetType.getName(), paramName);
                    T result = XmlMarshaller.deserialize(requestBody, targetType);
                    return ResolverResult.success(result);
            }
            // For non-JSON content types, try to convert using the standard converter
            _logger.debug("Attempting standard conversion of request body to type '{}' for parameter '{}'", 
                        targetType.getName(), paramName);
            return convertValue(requestBody, targetType, paramName);

        } catch (Exception e) {
            _logger.error("Error deserializing request body to type '{}' for parameter '{}': {}", 
                        targetType.getName(), paramName, e.getMessage(), e);
            
            String errorMessage = String.format("Error deserializing request body to type '%s'", targetType.getName());
            T defaultValue = handleError(targetType, requestBody, e);
            return ResolverResult.failed(defaultValue, 
                new BindingErrorImpl(paramName, errorMessage + ": " + e.getMessage(), requestBody));
        }
    }
}
