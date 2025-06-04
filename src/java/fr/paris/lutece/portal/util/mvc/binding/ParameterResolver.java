package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;

import jakarta.servlet.http.HttpServletRequest;

/**
 * A strategy interface for resolving method parameters in the context of an MVC request.
 * <p>
 * Implementations determine whether they can handle a specific parameter via {@link #supports(Parameter)}
 * and then provide the value through {@link #resolve(HttpServletRequest, Parameter)}.
 * </p>
 */
public interface ParameterResolver {

	/**
     * Checks if the resolver can handle the given parameter.
     * 
     * @param parameter the parameter to check
     * @return true if the resolver can handle the parameter, false otherwise
     */
	  boolean supports(Parameter parameter);
	  
	 /**
	  *  Resolves and converts the given parameter from the HTTP request.
	  * 
	  * @param request the HTTP request
	  * @param parameter the parameter to resolve
	  * @return the result of the resolution and conversion
	  */
	  ResolverResult<?>  resolve(HttpServletRequest request, Parameter parameter);
}
