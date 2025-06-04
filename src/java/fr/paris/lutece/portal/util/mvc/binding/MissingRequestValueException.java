package fr.paris.lutece.portal.util.mvc.binding;

/**
 * Exception thrown when a required parameter is missing
 */
public class MissingRequestValueException extends RuntimeException {
	private final String parameterName;
    private final String parameterType;
    
    public MissingRequestValueException(String message, String parameterName, String parameterType) {
        super(message);
    	this.parameterName = parameterName;
        this.parameterType = parameterType;
    }
    
    public String getParameterName() {
        return parameterName;
    }
    
    public String getParameterType() {
        return parameterType;
    }

}
