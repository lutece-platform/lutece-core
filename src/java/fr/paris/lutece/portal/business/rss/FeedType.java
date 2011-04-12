package fr.paris.lutece.portal.business.rss;

/**
 * Feed types
 */
public enum FeedType
{
	RSS09("rss_0.9"), 
	RSS10("rss_1.0"), 
	RSS20("rss_2.0"), 
	ATOM03("atom_0.3"), 
	ATOM10("atom_1.0");
	
	private String _strType;
	
	/**
	 * Sets the type (as String)
	 * @param strType the type
	 */
	private FeedType( String strType )
	{
		_strType = strType;
	}
	
	/**
	 * Gets the type as String
	 * @return type
	 */
	public String getType()
	{
		return _strType;
	}

}
