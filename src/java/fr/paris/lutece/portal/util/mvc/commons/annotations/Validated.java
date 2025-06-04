package fr.paris.lutece.portal.util.mvc.commons.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
/**Annotation to indicate that a parameter or field should be validated.
 * This can be used in the context of MVC lutece frameworks to apply validation rules
 * to method parameters or class fields.
 * 
 * <p>
 * The annotation can specify validation groups, allowing different validation
 * rules to be applied based on the context in which the parameter or field is used.
 * </p>
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validated {
	 /**
	 * Specifies the validation groups that this parameter or field belongs to.
	 * This can be used to apply different validation rules based on the context.
	 *
	 * @return An array of classes representing the validation groups
	 */
	 Class<?>[] value() default {};
}
