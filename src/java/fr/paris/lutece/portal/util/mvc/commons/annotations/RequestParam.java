package fr.paris.lutece.portal.util.mvc.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
	/**
	 * The name of the request parameter to bind to this method parameter.
	 * If not specified, the method parameter name will be used.
	 *
	 * @return The name of the request parameter
	 */
    String value() default "";
    /**
	 * Indicates whether this request parameter is required.
	 * If true, an error will be thrown if the parameter is not present in the request.
	 * If false, the parameter will be optional.
	 *
	 * @return true if the parameter is required, false otherwise
	 */
    boolean required() default true;
    /**
	 * The default value to use as a fallback.
	 * <p>Supplying a default value implicitly sets {@link #required} to
	 * {@code false}.
	 */ 
    String defaultValue() default MVCUtils.DEFAULT_NONE;
}