package fr.paris.lutece.portal.util.mvc.binding;

public interface BindingError extends ParamError {

	/**
     * Provides access to the raw submitted value of the parameter which caused the
     * binding to fail.
     *
     * @return The raw value submitted for the parameter
     */
    String getSubmittedValue();
}
