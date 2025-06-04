package fr.paris.lutece.portal.util.mvc.binding;

/**
 * Represents an error that occurs during the binding and validation process in the MVC framework.
 * This interface encapsulates details about the error, including the parameter name
 * and a human-readable error message.
 */
public interface ParamError {

	 /**
     * Returns a human-readable error message for this error.
     *
     * @return The human-readable error message
     */
    String getMessage();

    /**
     * The parameter name of the value that caused the error. This is usually
     * the name specified in the binding annotation.
     *
     * @return The name of the parameter which caused the error
     */
    String getParamName();
}
