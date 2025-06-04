package fr.paris.lutece.portal.util.mvc.binding;

import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class manages the lifecycle of BindingResultImpl. Especially it observes the
 * disposal of the current instance so it can warn the user about unconsumed errors.
 *
 */
@RequestScoped
public class BindingResultManager {

    private static final Logger _logger = MVCUtils.getLogger( );

    @Inject
    private HttpServletRequest request;

    @Produces
    @RequestScoped
    public BindingResultImpl createBindingResult() {
    	_logger.trace("Creating a new BindingResult for the request: " + request.getRequestURI());
        return new BindingResultImpl();
    }

    public void destroyBindingResult(@Disposes BindingResultImpl bindingResult) {

        if (bindingResult.hasUnconsumedErrors()) {

        	_logger.warn(
                "The request [{}] produced binding or validation errors but BindingResult was not consumed. " +
                    "This usually means that you forgot to check BindingResult for errors.",
                new Object[]{request.getRequestURI()}
            );

        }


    }

}
