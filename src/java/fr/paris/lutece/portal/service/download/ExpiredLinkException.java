package fr.paris.lutece.portal.service.download;

public class ExpiredLinkException extends Exception
{

    private static final long serialVersionUID = -4788782240985061910L;

    /**
     * Creates a new instance of ExpiredLinkException
     * 
     * @param strMessage
     *            The exception message
     */
    public ExpiredLinkException( String strMessage )
    {
        super( strMessage );
    }
}
