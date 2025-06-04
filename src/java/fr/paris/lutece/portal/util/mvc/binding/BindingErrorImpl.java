package fr.paris.lutece.portal.util.mvc.binding;
/**
 *  Represents an error that occurs during the binding process in the MVC lutece framework.
 * This class encapsulates details about the error, including the parameter name,
 * error message, and the submitted value that caused the binding to fail.
 */
public class BindingErrorImpl implements BindingError {

	 	private String message;

	    private String paramName;

	    private final String submittedValue;

	    public BindingErrorImpl(String paramName, String message,  String submittedValue) {
	        this.message = message;
	        this.paramName = paramName;
	        this.submittedValue = submittedValue;
	    }

	    @Override
	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    @Override
	    public String getParamName() {
	        return paramName;
	    }

	    public void setParamName(String paramName) {
	        this.paramName = paramName;
	    }

	    @Override
	    public String getSubmittedValue() {
	        return submittedValue;
	    }
}
