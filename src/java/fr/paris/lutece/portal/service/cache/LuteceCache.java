package fr.paris.lutece.portal.service.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;
/**
 * The {@code @LuteceCache} annotation is used to inject a cache instance into a component or method
 * in the Lutece framework. This annotation helps to configure and manage cache instances
 * by specifying cache properties and types directly in the code.
 * 
 * <p>When applied, this annotation allows the retrieval and injection of a cache instance based
 * on the specified cache name and type parameters.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @Inject
 * @LuteceCache(cacheName = "myCache", keyType = Integer.class, valueType = String.class, enable = enable)
 * Lutece107Cache<Integer, String> myCacheInstance;
 * }
 * </pre>
 * 
 * <p>In the example above, the annotation is used to inject a cache instance named "myCache"
 * with keys of type {@code Integer} and values of type {@code String}. The cache instance is
 * then available for use in the {@code myCacheInstance} field.</p>
 * 
 * <p>Attributes:</p>
 * <ul>
 *     <li>{@code cacheName}: The name of the cache to be injected. This is a required attribute.</li>
 *     <li>{@code keyType}: The type of the cache keys. Defaults to {@code Object.class} if not specified.</li>
 *     <li>{@code valueType}: The type of the cache values. Defaults to {@code Object.class} if not specified.</li>
 *     <li>{@code enable}: A boolean flag indicating whether the cache injection should be enabled. Defaults to {@code false}.</li>
 * </ul>
 * 
 * <p>This annotation is particularly useful for configuring and managing caches in a dependency injection
 * context, ensuring that the correct cache instance is injected based on the specified configuration.</p>
 * 
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface LuteceCache{

	/**
     * The name of the cache to be injected.
     * 
     * @return the cache name
     */
	@Nonbinding
	String cacheName();
	
	/**
     * The type of keys used in the cache.
     * 
     * @return the key type
     */	
	@Nonbinding
    Class<?> keyType() default Object.class;
	
	/**
     * The type of values used in the cache.
     * 
     * @return the value type
     */
	@Nonbinding
    Class<?> valueType() default Object.class;
	
	/**
     * Flag indicating whether cache injection is enabled.
     * 
     * @return {@code true} if cache injection is enabled, {@code false} otherwise
     */
	@Nonbinding
    boolean enable() default false;

	/**
	 * Flag indicating whether the cache can be reset by global cache resets
	 * 
	 * @return {@code true} if cache reset is prevented, {@code false} otherwise
	 */
	@Nonbinding
	boolean preventGlobalReset() default false;
}
