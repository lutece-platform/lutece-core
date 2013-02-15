package fr.paris.lutece.portal.service.csv;

/**
 * Describe an error that occurred during the reading of a CSV file.
 */
public class CSVMessageDescriptor implements Comparable<CSVMessageDescriptor>
{
    private CSVMessageLevel _messageLevel;
    private int _nLineNumber;
    private String _strMessageContent;

    /**
     * Default constructor
     */
    public CSVMessageDescriptor( )
    {

    }

    /**
     * Creates a new <i>CSVMessageDescriptor</i> with every attributes
     * initialized
     * @param messageLevel The level of the message
     * @param nLineNumber The number of the line associated with the message
     * @param strMessageContent The content of the message
     */
    public CSVMessageDescriptor( CSVMessageLevel messageLevel, int nLineNumber, String strMessageContent )
    {
        this._messageLevel = messageLevel;
        this._nLineNumber = nLineNumber;
        this._strMessageContent = strMessageContent;
    }

    /**
     * Get the level of the message
     * @return The level of the message
     */
    public CSVMessageLevel getMessageLevel( )
    {
        return _messageLevel;
    }

    /**
     * Set the level of the error
     * @param errorLevel the level of the error
     */
    public void setMessageLevel( CSVMessageLevel messageLevel )
    {
        this._messageLevel = messageLevel;
    }

    /**
     * Get the number of the line associated with this error.
     * @return The number of the line associated with this error.
     */
    public int getLineNumber( )
    {
        return _nLineNumber;
    }

    /**
     * Set the number of the line associated with this error.
     * @param nLineNumber The number of the line associated with this error.
     */
    public void setLineNumber( int nLineNumber )
    {
        this._nLineNumber = nLineNumber;
    }

    /**
     * Get the description of the message
     * @return The description of the message
     */
    public String getMessageContent( )
    {
        return _strMessageContent;
    }

    /**
     * Set the description of the message
     * @param strMessageContent The description of the message
     */
    public void setMessageContent( String strMessageContent )
    {
        this._strMessageContent = strMessageContent;
    }

    /**
     * compare this CSVMessageDescriptor with another.
     * @param o Object to compare to
     * @return Returns :<br />
     *         <ul>
     *         <li>
     *         -1 if the line number of this object is greater than the line
     *         number of the other object, or if this object has an
     *         {@link CSVMessageLevel#INFO INFO} level and the other one an
     *         {@link CSVMessageLevel#ERROR ERROR} level if they have the same
     *         line number.</li>
     *         <li>
     *         0 if they both have the same line number and level, regardless of
     *         their description</li>
     *         <li>1 if the other object is null, if its line number if greater
     *         than the line number of the current object, or if this object has
     *         a {@link CSVMessageLevel#ERROR ERROR} level whereas the other has
     *         the {@link CSVMessageLevel#INFO INFO} level if they have the same
     *         line number.</li>
     *         </ul>
     */
    @Override
    public int compareTo( CSVMessageDescriptor o )
    {
        if ( o == null )
        {
            return 1;
        }
        if ( this.getLineNumber( ) == o.getLineNumber( ) )
        {
            if ( this.getMessageLevel( ) == CSVMessageLevel.ERROR )
            {
                if ( o.getMessageLevel( ) == CSVMessageLevel.ERROR )
                {
                    return 0;
                }
                else
                {
                    return 1;
                }
            }
            else
            {
                if ( o.getMessageLevel( ) == CSVMessageLevel.INFO )
                {
                    return 0;
                }
                else
                {
                    return -1;
                }
            }
        }
        else
        {
            if ( this.getLineNumber( ) > o.getLineNumber( ) )
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
    }
}
