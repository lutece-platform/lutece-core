package fr.paris.lutece.portal.util.mvc.binding;

import java.util.List;
import java.util.Objects;

/**
 * A wrapper object that holds the result of a parameter resolution.
 *
 * @param <T> the type of the resolved value
 */
public class ResolverResult<T> {

	private final T value;
    private final List<BindingError> errors;

    /**
     * Creates a result with the given value and binding errors.
     *
     * @param value  the resolved value (may be null)
     * @param errors the list of binding errors
     * @return a {@link ResolverResult} with errors
     */
    private ResolverResult(T value, List<BindingError> error) {
        this.value = value;
        this.errors = error;
    }
    /**
     * Creates a successful result with the resolved value and no errors.
     *
     * @param value the resolved value
     * @return a successful {@link ResolverResult}
     */
    public static <T> ResolverResult<T> success(T value) {
        return new ResolverResult<>(value, null);
    }
    /**
     * Creates a result with the given value and binding errors.
     *
     * @param value  the resolved value (may be null)
     * @param errors the list of binding errors
     * @return a {@link ResolverResult} with errors
     */
    public static <T> ResolverResult<T> failed(T value, List<BindingError> errors) {
        return new ResolverResult<>(value, Objects.requireNonNull(errors, "Error must not be null"));
    }
    /**
     * Creates a result with the given value and binding errors.
     *
     * @param value  the resolved value (may be null)
     * @param error of binding 
     * @return a {@link ResolverResult} with errors
     */
    public static <T> ResolverResult<T> failed(T value, BindingError error) {
    	List<BindingError> errors = List.of(error);
        return failed(value,  errors);
    }
    /**
     * Returns the resolved value.
     *
     * @return the resolved value, or {@code null} if resolution failed
     */
    public Object getValue() {
        return value;
    }
    /**
     * Returns the list of binding errors, if any.
     *
     * @return a list of errors, or an empty list if there were none
     */
    public List<BindingError> getErrors() {
        return errors;
    }

}
