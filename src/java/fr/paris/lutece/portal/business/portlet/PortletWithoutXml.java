package fr.paris.lutece.portal.business.portlet;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.message.SiteMessageException;


/**
 * Class that allow templates to generate content without using XML and XSL.
 */
public abstract class PortletWithoutXml extends Portlet
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getXml( HttpServletRequest request ) throws SiteMessageException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXmlDocument( HttpServletRequest request ) throws SiteMessageException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isContentGeneratedByXmlAndXsl( )
    {
        return false;
    }

    /**
     * Get the HTML content of the portlet
     * @param request The request
     * @return The HTML content of the portlet
     */
    @Override
    public abstract String getHtmlContent( HttpServletRequest request );
}
