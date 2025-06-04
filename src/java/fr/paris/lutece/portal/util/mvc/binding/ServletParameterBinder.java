package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.util.mvc.utils.BindingUtils;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Handles the binding of method parameters from an {@link HttpServletRequest}.
 * <p>
 * This class uses available {@link ParameterResolver} implementations to resolve and bind method parameters
 * in the context of an MVC controller. It supports standard types like {@link HttpServletRequest}, and
 * delegates other parameter types to custom resolvers. If no resolver is found, a binding error is recorded.
 * </p>
 *
 * <p>This class is application-scoped and managed by CDI.</p>
 */
@ApplicationScoped
public class ServletParameterBinder {

	@Inject
    @Any
    private Instance<ParameterResolver> parameterResolvers;
	@Inject
	private BindingResultImpl bindingResult;	
    private static final Logger _logger = MVCUtils.getLogger( );

    /**
    * Binds the parameters of the given method using values from the {@link HttpServletRequest}.
    * <p>
    * Each method parameter is matched against a set of {@link ParameterResolver}s. If a matching resolver
    * is found, it is used to resolve the value; otherwise, a binding error is logged and recorded.
    * The special case of {@link HttpServletRequest} parameters is handled directly.
    * </p>
    *
    * @param request The HTTP servlet request containing the input data.
    * @param method  The method whose parameters are to be bound.
    * @return An array of arguments corresponding to the method's parameters, with resolved values or {@code null} if unresolved.
    */
	 public Object[] bindParameters( HttpServletRequest request, Method method)  {

		    Parameter[] parameters = method.getParameters();
	        Object[] args = new Object[parameters.length];

	        for (int i = 0; i < parameters.length; i++) {
	        	Parameter param = parameters[i];
	            if (HttpServletRequest.class.isAssignableFrom(param.getType())) {
	                args[i] = request;
	                continue;
	            }        
	            ResolverResult<?> result= parameterResolvers.stream()
	            		.filter(r -> r.supports(param))
	                    .findFirst()
	                    .map(r -> r.resolve(request, param))
	                    .orElse(null);
	            if(result == null) {
	            	// If no resolver is found, we can handle the error here
	                String paramName =  BindingUtils.getEffectiveParameterName( param );
	                String submittedValue = request.getParameter(paramName);
	                _logger.error("No resolver found for parameter '{}'", paramName);
	                bindingResult.addBindingError(new BindingErrorImpl(paramName, "No resolver found", submittedValue));
	                args[i] = null;
	             } else if(result.getErrors() != null && !result.getErrors().isEmpty( )) {
	                bindingResult.addBindingError( result.getErrors() );
	                args[i] = result.getValue();
	             }
	             else {
	                	args[i] = result.getValue();
	             }
	        }
	        return args;
	 }
}
