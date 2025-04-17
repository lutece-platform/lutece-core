package fr.paris.lutece.portal.web.cdi.mvc;

import java.util.Map;

/**
 * <p>A map of name to model instances used by a to process a view. Instances implementing this interface must be injectable using
 * {@link jakarta.inject.Inject} and are {@link jakarta.enterprise.context.RequestScoped}.
 *
 * <p>Note that certain view engines, such as the engine for Jakarta Server Pages, support
 * model binding via {@link jakarta.inject.Named} in which case the use of Models is
 * optional.
 *
 * @see jakarta.inject.Named
 * @see jakarta.enterprise.context.RequestScoped
 * @since 8.0.0
 */
public interface Models extends Iterable<String> {

	 /**
     * Stores a new model in the map.
     *
     * @param name  name of the model
     * @param model model to store in the map
     * @return the current instance to allow method chaining
     */
    Models put(String name, Object model);

    /**
     * Retrieve a model by name.
     *
     * @param name name of the model
     * @return the model or <code>null</code>
     */
    Object get(String name);

    /**
     * Retrieve a model by name in a type-safe way.
     *
     * @param name  name of the model
     * @param clazz type of the model
     * @param <T>   type of the model
     * @return The model or <code>null</code>
     */
    <T> T get(String name, Class<T> clazz);

    /**
     * Returns a unmodifiable view of the models map.
     *
     * @return unmodifiable map
     */
    Map<String, Object> asMap();
}
