package fr.paris.lutece.portal.service.util;

public class PathNotFoundException extends RuntimeException {

	@java.io.Serial
    static final long serialVersionUID = 7999581764786402595L;

    /**
     * Constructs an instance of this class.
     */
    public PathNotFoundException() {
    }

    /**
     * Constructs an instance of this class.
     *
     * @param   msg
     *          the detail message
     */
    public PathNotFoundException(String msg) {
        super(msg);
    }
    
    public PathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public PathNotFoundException(Throwable cause) {
        super(cause);
    }
}
