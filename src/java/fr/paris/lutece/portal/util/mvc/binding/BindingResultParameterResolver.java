package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

/** * This class resolves parameters of type {@link BindingResult} in the context of
 * a web request. It is used to inject a BindingResult instance into methods
 * annotated with MVC annotations.
 * 
 * <p>
 * The BindingResultParameterResolver is responsible for providing an instance of
 * BindingResult when requested, allowing for validation and error handling in
 * MVC applications.
 * </p>
 */
@ApplicationScoped
public class BindingResultParameterResolver implements ParameterResolver {

	private final Instance<BindingResult> bindingResultInstance;

	/**
	 * Constructor for BindingResultParameterResolver.
	 * 
	 * @param bindingResultInstance the instance of BindingResult to be injected
	 */
	@Inject
	public BindingResultParameterResolver(Instance<BindingResult> bindingResultInstance) {
	    this.bindingResultInstance = bindingResultInstance;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
    public boolean supports(Parameter parameter) {
        return BindingResult.class.isAssignableFrom(parameter.getType());
    }
	/**
	 * {@inheritDoc}
	 */
    @Override
    public ResolverResult<BindingResult>  resolve(HttpServletRequest request, Parameter parameter) {
    	BindingResult result = bindingResultInstance.get();
        if (result == null) {
            throw new IllegalStateException("No BindingResult instance available.");
        }
    	return ResolverResult.success( result );
    }

}
