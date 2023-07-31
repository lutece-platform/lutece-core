package fr.paris.lutece.portal.service.util;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
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

}
