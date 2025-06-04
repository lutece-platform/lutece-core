package fr.paris.lutece.portal.util.mvc.binding.validate;

import java.util.Iterator;

import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.cdi.mvc.MvcInternal;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

/**
 * Produces Bean Validation validation objects. Will try to get the default ones provided by the
 * Jakarta EE contrainer and falls back to build a custom ones.
 *
 */
@ApplicationScoped
public class BeanValidationProducer {

    private static final Logger _logger = MVCUtils.getLogger( );

    /**
     * The ValidatorFactory should be available for injection as defined here:
     * http://beanvalidation.org/1.1/spec/#d0e11327
     */
    @Inject
    private Instance<ValidatorFactory> validatorFactoryInstance;

    /**
     * The actual ValidatorFactory to use
     */
    private ValidatorFactory validatorFactory;

    /**
     * We should be able to get a ValidatorFactory from the container in an jakarta EE environment.
     * However, if we don't get the factory, we will will use a default one. This is especially
     * useful for non jakarta EE environments and in the CDI tests
     */
    @PostConstruct
    public void init() {

        // Prefer the ValidatorFactory provided by the container
        Iterator<ValidatorFactory> iterator = validatorFactoryInstance.iterator();
        if (iterator.hasNext()) {
            this.validatorFactory = iterator.next();
        }

        // create a default factory if we didn't get one
        else {
        	_logger.warn("Creating a ValidatorFactory because the container didn't provide one!");
            this.validatorFactory = Validation.buildDefaultValidatorFactory();
        }

    }

    @Produces
    @MvcInternal
    @ApplicationScoped
    public ValidatorFactory produceValidationFactory() {
        return validatorFactory;
    }
}