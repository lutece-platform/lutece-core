package fr.paris.lutece.portal.business.globalmanagement;

/**
 * Rich text editor configuration
 */
public class RichTextEditor
{
	private String _strEditorName;
	private String _strDescription;
	private boolean _bBackOffice;

	public String getEditorName( )
	{
		return _strEditorName;
	}

	public void setEditorName( String strEditorName )
	{
		_strEditorName = strEditorName;
	}

	public String getDescription( )
	{
		return _strDescription;
	}

	public void setDescription( String strDescription )
	{
		_strDescription = strDescription;
	}

	/**
	 * Get a boolean describing of this text editor should be used for back of front office
	 * @return True if this editor should be used for back office, false if it should be used for front office.
	 */
	public boolean getBackOffice( )
	{
		return _bBackOffice;
	}

	/**
	 * Set a boolean describing of this text editor should be used for back of front office
	 * @param bBackOffice True if this editor should be used for back office, false if it should be used for front office.
	 */
	public void setBackOffice( boolean bBackOffice )
	{
		_bBackOffice = bBackOffice;
	}
}
