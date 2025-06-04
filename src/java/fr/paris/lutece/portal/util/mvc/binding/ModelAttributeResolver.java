package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.util.mvc.commons.annotations.ModelAttribute;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.util.bean.BeanUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;
/** This class resolves parameters annotated with {@link ModelAttribute} in the context
 * of a web request. It extracts model attributes from the request and converts them
 * to the appropriate type, handling binding errors.
 * 
 * <p>
 * The ModelAttributeResolver is used to inject model attributes into methods annotated with
 * MVC annotations, allowing for easy access to model data in web applications.
 * </p>
 */
@ApplicationScoped
public class ModelAttributeResolver implements ParameterResolver {
	
		private static final Logger _logger = MVCUtils.getLogger( );
	    /**
	     * {@inheritDoc}
	     */
		@Override
	    public boolean supports(Parameter parameter) {
	        return parameter.isAnnotationPresent(ModelAttribute.class);
	    }	
		/**
	     * {@inheritDoc}
	     */
	    @Override
	    public ResolverResult<?> resolve(HttpServletRequest request, Parameter parameter) {
	        Class<?> type = parameter.getType();
		    List<BindingError> errors = new ArrayList<>();
	        ModelAttribute annotation = parameter.getAnnotation(ModelAttribute.class);
	        String prefix = annotation.value().isEmpty() ? "" : annotation.value() + ".";

	        try {
	            Object instance = type.getDeclaredConstructor().newInstance();
                Map<String, String[]> parameterMap= BeanUtil.convertMap(request.getParameterMap());
	            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
	                String key = entry.getKey();

	                if (prefix.isEmpty() || key.startsWith(prefix)) {
	                    String propertyName = prefix.isEmpty() ? key : key.substring(prefix.length());
	                    String[] values = entry.getValue();

	                    if (values != null ) {
	                        try {
	                        	BeanUtil.getEnhancedBean(LocalVariables.getLocale( )).setProperty(instance, propertyName, values);
	                        } catch (Exception e) {
	                        	_logger.debug("Error assigning property '{}' with values '{}'", propertyName, values, e);
	                        	errors.add( new BindingErrorImpl(propertyName, e.getMessage(), Arrays.toString(values)));
	                        }
	                    }
	                }
	            }

	            return errors.isEmpty() ? 
			    		ResolverResult.success(instance) : 
			    			ResolverResult.failed(instance, errors); 
	        } catch (Exception e) {
	        	_logger.error("Error creating or populating object of type {}", type.getName(), e);
	            throw new IllegalArgumentException("Error creating or populating object of type " + type.getName(), e);
	        }
	        
	    }
}
