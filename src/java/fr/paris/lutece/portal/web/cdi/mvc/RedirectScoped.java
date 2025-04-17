package fr.paris.lutece.portal.web.cdi.mvc;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import jakarta.enterprise.context.NormalScope;

/**
 * An annotation that defines a new Jakarta Contexts and Dependency Injection-based 
 * scope supported by the Lutece MVC API.
 * Beans in this scope are automatically created and destroyed by correlating
 * a redirect and the request that follows. The exact mechanism by which requests
 * are correlated is implementation dependent, but popular techniques include URL
 * rewrites and cookies.
 * 
 */
@NormalScope(passivating = true)
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RedirectScoped {

}
