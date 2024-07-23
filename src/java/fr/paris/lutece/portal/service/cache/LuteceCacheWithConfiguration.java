package fr.paris.lutece.portal.service.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import jakarta.inject.Qualifier;
/**
 * The {@code @LuteceCacheWithConfiguration} annotation is used to mark components in the Lutece system
 * to indicate that they should be associated with a specific cache configuration.
 * 
 * This annotation can be applied to various elements in the source code, including:
 * <ul>
 *     <li>Classes ({@code @Target(ElementType.TYPE)})</li>
 *     <li>Fields ({@code @Target(ElementType.FIELD)})</li>
 *     <li>Method parameters ({@code @Target(ElementType.PARAMETER)})</li>
 *     <li>Methods ({@code @Target(ElementType.METHOD)})</li>
 * </ul>
 * 
 * <p>Annotations like this one are commonly used for integrating specific functionalities
 * such as dynamic configuration or cache management aspects.</p>
 * 
 * <p>It is important to note that this annotation is for marking purposes only,
 * and its significance should be interpreted by a configuration or processing mechanism
 * in the context of cache management in Lutece.</p>
 * 
 * @see fr.paris.lutece.portal.service.cache.LuteceCacheManager.createCache( String strCacheName, C configuration, boolean enable ) 
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface LuteceCacheWithConfiguration{

}
