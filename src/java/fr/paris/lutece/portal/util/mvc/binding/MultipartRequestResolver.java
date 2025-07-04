package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;

import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.http.MultipartUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Resolver for parameters of type {@link MultipartHttpServletRequest}.
 * <p>
 * This class is responsible for resolving multipart HTTP requests in the context of an MVC request.
 * It checks if the parameter type is {@link MultipartHttpServletRequest} and resolves the request object.
 * </p>
 *
 * <p>
 * If the request is not of type {@link MultipartHttpServletRequest}, a {@link ResolverResult#failed}
 * is returned with an appropriate error message.
 * </p>
 *
 * <p>
 * This class is annotated with {@link ApplicationScoped}, meaning it is managed by the CDI container and shared
 * across the application.
 * </p>
 *
 * @see ParameterResolver
 * @see MultipartHttpServletRequest
 * @see ResolverResult
 */
@ApplicationScoped
public class MultipartRequestResolver implements ParameterResolver {
	
	/**
     * Determines whether this resolver supports the given parameter.
     * <p>
     * This method checks if the parameter type is {@link MultipartHttpServletRequest}.
     * </p>
     *
     * @param parameter The method parameter to check.
     * @return {@code true} if the parameter type is {@link MultipartHttpServletRequest}, {@code false} otherwise.
     */
	@Override
	public boolean supports(Parameter parameter) {
        return MultipartHttpServletRequest.class.isAssignableFrom(parameter.getType());

	}
	/**
     * Resolves the value of the given parameter from the {@link HttpServletRequest}.
     * <p>
     * This method casts the request to {@link MultipartHttpServletRequest} if possible and returns it.
     * If the request is not of type {@link MultipartHttpServletRequest}, a {@link ResolverResult#failed}
     * is returned with an appropriate error message.
     * </p>
     *
     * @param request   The HTTP request containing the multipart data.
     * @param parameter The method parameter to resolve.
     * @return A {@link ResolverResult} containing the resolved {@link MultipartHttpServletRequest} or an error if resolution fails.
     */
	@Override
	public ResolverResult<?> resolve(HttpServletRequest request, Parameter parameter) {
		if ((request instanceof MultipartHttpServletRequest)) {
            // Cast the request to MultipartHttpServletRequest
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            return ResolverResult.success(multipartRequest);
        } else {
            // Return a failed result if the request is not multipart
            return ResolverResult.failed(null, new BindingErrorImpl(parameter.getName(), 
                "Request is not MultipartHttpServletRequest", null));
        }
	}

}
