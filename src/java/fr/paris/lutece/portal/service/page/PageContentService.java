package fr.paris.lutece.portal.service.page;

import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Parameters;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author pierre
 */
public class PageContentService extends ContentService

{
    private static final String SERVICE_NAME = "Page Content Service";

    private IPageService _pageService = (IPageService) SpringContextService.getBean( "pageService" );


    @Override
    public String getPage(HttpServletRequest request, int nMode) throws UserNotSignedException, SiteMessageException
    {
        return _pageService.getPage(request, nMode);
    }

    /**
     * Analyzes request's parameters to see if the request should be handled by the current Content Service
     *
     * @param request The HTTP request
     * @return true if this ContentService should handle this request
     */
    public boolean isInvoked(HttpServletRequest request)
    {
        String strPageId = request.getParameter(Parameters.PAGE_ID);

        if ((strPageId != null) && (strPageId.length() > 0))
        {
            return true;
        }

        return false;
    }


    public String getName()
    {
        return SERVICE_NAME;
    }

}
