package fr.paris.lutece.portal.service.util;


import java.lang.reflect.Type;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;

public class CdiHelper {

    private static BeanManager beanManager;

    /**
     * <p>
     * Obtains a contextual reference for a certain EL name and a certain bean type of the bean.
     * </p>
     *
     * @param beanType a bean type that must be implemented by any client proxy that is returned
     * @param name the EL name
     * @return a contextual reference 
     * @throws IllegalArgumentException if the given type is not a bean type of the given bean
     * @throws IllegalStateException    if called during application initialization, before the {@link AfterDeploymentValidation}
     *                                  event is fired.
     */
    public static <T> T getReference( Type beanType, String name ) {
    	if (beanManager == null) {
    		beanManager = CDI.current().getBeanManager();
    	}
    	Bean<?> bean=  beanManager.resolve( beanManager.getBeans(name));        	
  		CreationalContext<?> creatinalContext= beanManager.createCreationalContext(bean);            		
  		return (T) beanManager.getReference(bean, beanType, creatinalContext);	
    }
    /**
	 * <p>
	 * Obtains a contextual reference for a certain bean type of the bean.
	 * </p>
	 *
	 * @param clazz the class of the bean
	 * @return a contextual reference
	 */
    public static <T> T getBean(Class<T> clazz) {
        return CDI.current().select(clazz).get();
    }

    /**
     * Generic service resolution using CDI Instance&lt;T&gt;.
     * Provides proper error handling and logging for missing services.
     *
     * @param <T> Service type
     * @param instance CDI Instance container
     * @param serviceName Name/qualifier of the service to resolve
     * @return resolved service instance
     * @throws IllegalStateException if service cannot be resolved
     */
    public static <T> T resolve(Instance<T> instance, String serviceName) {
        Instance<T> qualified = instance.select(NamedLiteral.of(serviceName));
        if (qualified.isUnsatisfied()) {
            throw new IllegalStateException(String.format(
                "No instance found for name '%s'.", serviceName));
        }
        if (qualified.isAmbiguous()) {
            throw new IllegalStateException(String.format(
                "Multiple instance found for name '%s'.",  serviceName));
        }
        return qualified.get();
    }
}
