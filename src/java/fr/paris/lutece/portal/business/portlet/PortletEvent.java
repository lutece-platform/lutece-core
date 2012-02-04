package fr.paris.lutece.portal.business.portlet;

/**
 * Portlet event
 *
 */
public class PortletEvent
{
	public static final int INVALIDATE = 1;
	private int _nType;
	private int _nPortletId;
	private int _nPageId;
	
	/**
	 * Default empty constructor
	 */
	public PortletEvent()
	{
		// nothing
	}
	
	/**
	 * PortletEvent
	 * @param nType the type
	 * @param nPortletId the portlet id
	 * @param nPageId the page id
	 */
	public PortletEvent( int nType, int nPortletId, int nPageId )
	{
		_nType = nType;
		_nPortletId = nPortletId;
		_nPageId = nPageId;
	}

	/**
	 * Event type
	 * @return event type
	 */
	public int getType(  )
	{
		return _nType;
	}

	/**
	 * Sets the event type
	 * @param nType event type
	 */
	public void setType( int nType )
	{
		this._nType = nType;
	}

	/**
	 * Gets the portlet id
	 * @return the portlet id
	 */
	public Integer getPortletId(  ) 
	{
		return _nPortletId;
	}

	/**
	 * Sets the portlet id
	 * @param nPortletId the portlet id
	 */
	public void setPortletId( int nPortletId ) 
	{
		this._nPortletId = nPortletId;
	}
	
	/**
	 * Gets the page id
	 * @return the page id
	 */
	public int getPageId(  )
	{
		return _nPageId;
	}
	
	/**
	 * Sets the page id
	 * @param nPageId the page id
	 */
	public void setPageId( int nPageId )
	{
		_nPageId = nPageId;
	}
}
