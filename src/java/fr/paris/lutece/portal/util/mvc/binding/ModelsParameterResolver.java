package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;

import fr.paris.lutece.portal.web.cdi.mvc.Models;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
/** This class resolves parameters of type {@link Models} in the context of a web request.
 * It is used to inject a Models instance into methods annotated with MVC annotations.
 *
 * <p>
 * The ModelsParameterResolver is responsible for providing an instance of Models when requested,
 * allowing for easy access to model data in MVC applications.
 * </p>
 */
@ApplicationScoped
public class ModelsParameterResolver implements ParameterResolver {

	private final Instance<Models> modelInstance;

	/** 
	 * Constructor for ModelsParameterResolver.
	 * 
	 * @param modelInstance the instance of Models to be injected
	 */
    @Inject
    public ModelsParameterResolver(Instance<Models> modelInstance) {
        this.modelInstance = modelInstance;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
    public boolean supports(Parameter parameter) {
        return Models.class.isAssignableFrom(parameter.getType());
    }
	/**
    * {@inheritDoc}
    */
    @Override
    public ResolverResult<Models> resolve(HttpServletRequest request, Parameter parameter) {
    	 Models models = modelInstance.get();
         if (models == null) {
             throw new IllegalStateException("No Models instance available.");
         }
         return ResolverResult.success(models);
    }

}
