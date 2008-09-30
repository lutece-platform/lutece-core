package fr.paris.lutece.portal.service.portlet;

import java.util.ArrayList;
import java.util.Collection;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.page.PageResourceIdService;
import fr.paris.lutece.portal.service.page.PageService;

/**
 * class PortletService
 */
public class PortletService 
{
	 private static PortletService _singleton;
	 
	 
	 private PortletService()
	 {
		 
	 }
	 /**
	     * Get the unique instance of the service
	     *
	     * @return The unique instance
	     */
	  public static synchronized PortletService getInstance(  )
	    {
	        if( _singleton==null)
	        {
	        	_singleton=new PortletService(); 
	        }
	        return _singleton;
	    }

	 
	/**
	 * Filter a collection of portlet  associated to a given user
	 * @param collectionPortlet the The collection to filter
	 * @param user the current user
	 * @return  a collection of portlet associated to a given user
	 */
	 public  Collection<Portlet> getAuthorizedPortletCollection(Collection<Portlet> collectionPortlet,AdminUser user)
	 {
		 Collection<Portlet> collectionPortletAuthorized=new ArrayList<Portlet>();
		 Page page;
		 for(Portlet portlet : collectionPortlet)
		 {
			page=PageHome.findByPrimaryKey(portlet.getPageId());
			if( PageService.getInstance().isAuthorizedAdminPage( page.getId(),PageResourceIdService.PERMISSION_VIEW,user ))
			{
				collectionPortletAuthorized.add(portlet);
			}
		}
		 return collectionPortletAuthorized;
	 }
	    /**
		 * Check if a portlet should be visible to the user 
		 * @param idPortlet the id of the portlet
		 * @param user the current user
		 * @return true if authorized, otherwise false
		 */
		 public  boolean isAuthorized(int idPortlet,AdminUser user)
		 {
			
			 	Page page=PageHome.getPageByIdPortlet( idPortlet );
			 	return  PageService.getInstance().isAuthorizedAdminPage( page.getId(),PageResourceIdService.PERMISSION_VIEW,  user );
				
		}
		  /**
			 * Check if a portlet should be visible to the user 
			 * @param portlet the portlet
			 * @param user the current user
			 * @return true if authorized, otherwise false
			 */
			 public boolean isAuthorized(Portlet portlet,AdminUser user)
			 {
				
				 Page page=PageHome.findByPrimaryKey(portlet.getPageId());
				 return   PageService.getInstance().isAuthorizedAdminPage( page.getId(),PageResourceIdService.PERMISSION_VIEW,user );
					
			}
}
