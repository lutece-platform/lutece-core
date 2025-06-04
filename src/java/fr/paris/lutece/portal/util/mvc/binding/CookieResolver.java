package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

import fr.paris.lutece.portal.util.mvc.commons.annotations.CookieValue;
import fr.paris.lutece.portal.util.mvc.utils.BindingUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class resolves parameters annotated with {@link CookieValue} in the context
 * of a web request. It extracts cookie values from the request and converts them
 * to the appropriate type, handling default values and required cookies.
 * 
 * <p>
 * The CookieResolver is used to inject cookie values into methods annotated with
 * MVC annotations, allowing for easy access to cookie data in web applications.
 * </p>
 */
@ApplicationScoped
public class CookieResolver extends AbstractParameterResolver {

    private static final String ERROR_REQUIRED_COOKIE = "The cookie '%s' is required but not provided";

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean supports(Parameter parameter) {
        return parameter.isAnnotationPresent(CookieValue.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolverResult<?> resolve(HttpServletRequest request, Parameter parameter) {
        if (_logger.isDebugEnabled()) {
            _logger.debug(LOG_RESOLVING_PARAMETER, parameter.getName(), parameter.getType().getName());
        }

        CookieValue annotation = parameter.getAnnotation(CookieValue.class);
        String cookieName = BindingUtils.getEffectiveParameterName(parameter);
        Class<?> targetType = parameter.getType();

        // Extract cookie value
        String rawValue = extractCookieValue(request, cookieName);

        // Validate and handle default value
        rawValue = validateAndPrepareValue(rawValue, cookieName, annotation, targetType);
        if (rawValue == null) {
            return getDefaultValue(targetType);
        }

        // Convert to target type
        return convertValue(rawValue, targetType, cookieName);
    }
    /**
	 * Extracts the value of a cookie from the request.
	 *
	 * @param request the HTTP servlet request
	 * @param cookieName the name of the cookie to extract
	 * @return the value of the cookie, or null if not found
	 */
    private String extractCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                                        .filter(c -> c.getName().equals(cookieName))
                                        .findFirst();
        return cookie.map(Cookie::getValue).orElse(null);
    }

    /**
	 * Validates the raw cookie value and prepares it for use.
	 *
	 * @param rawValue the raw value of the cookie
	 * @param cookieName the name of the cookie
	 * @param annotation the CookieValue annotation
	 * @param targetType the target type to convert to
	 * @return a validated and prepared value, or null if no value is provided
	 */
	 private String validateAndPrepareValue(String rawValue, String cookieName, 
                                           CookieValue annotation, Class<?> targetType) {
        boolean noValue = (rawValue == null || rawValue.isEmpty());

        if (noValue) {
            _logger.debug(LOG_NO_VALUES, cookieName);

            // Check if cookie is required
            if (annotation.required() && annotation.defaultValue().isEmpty()) {
                _logger.error(LOG_REQUIRED_MISSING, cookieName, targetType.getName());
                throw new MissingRequestValueException(String.format(ERROR_REQUIRED_COOKIE, cookieName), cookieName, targetType.getName());
            }
            // Use default value if specified
            if (!annotation.defaultValue().isEmpty()) {
                _logger.debug(LOG_DEFAULT_VALUE, annotation.defaultValue(), cookieName);
                return annotation.defaultValue();
            }

            return null;
        }

        _logger.debug(LOG_SINGLE_VALUE, cookieName);
        return rawValue;
    }
}

