/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects AliasPortlet
 */
public class AliasPortlet extends Portlet
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private int _nAliasId;

    /**
     * Sets the identifier of the portlet type to the value specified in the AliasPortletHome class
     */
    public AliasPortlet(  )
    {
        setPortletTypeId( AliasPortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * Sets the alias identifier of the portlet with the specified int value
     *
     * @param nAliasId the Alias identifier
     */
    public void setAliasId( int nAliasId )
    {
        _nAliasId = nAliasId;
    }

    /**
     * Returns the alias identifier of the portlet
     *
     * @return the Alias identifier
     */
    public int getAliasId(  )
    {
        return _nAliasId;
    }

    /**
     * Returns the Xml code of the Alias portlet without XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the Alias portlet content
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getXml( HttpServletRequest request )
        throws SiteMessageException
    {
        int nAliasId = AliasPortletHome.getAliasId( getId(  ) );

        //gets the portlet parent
        Portlet portlet = PortletHome.findByPrimaryKey( nAliasId );
        String strXmlAlias = portlet.getXml( request );
        String strTagPortletName = "</" + TAG_PORTLET_NAME + ">";
        int indexDeb = strXmlAlias.indexOf( strTagPortletName );
        int indexFin = strXmlAlias.indexOf( "</" + TAG_PORTLET + ">" );
        strXmlAlias = strXmlAlias.substring( indexDeb + strTagPortletName.length(  ), indexFin );

        StringBuffer buffXml = new StringBuffer(  );
        XmlUtil.beginElement( buffXml, TAG_PORTLET );
        XmlUtil.addElement( buffXml, TAG_PORTLET_NAME, getName(  ) );
        buffXml.append( strXmlAlias );
        XmlUtil.endElement( buffXml, TAG_PORTLET );

        return buffXml.toString(  );
    }

    /**
     * Returns the Xml code of the Alias portlet with XML heading
     *
     * @param request The HTTP Servlet request
     * @return the Xml code of the Alias portlet
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getXmlDocument( HttpServletRequest request )
        throws SiteMessageException
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Gets the XSl parameters of the alias portlet and returns them in form of a dictionnary
     *
     * @return the Xsl params
     */
    public Map<String, String> getXslParams(  )
    {
        int nIdAlias = AliasPortletHome.getAliasId( getId(  ) );

        //gets the portlet parent
        Portlet portlet = PortletHome.findByPrimaryKey( nIdAlias );

        return portlet.getXslParams(  );
    }

    /**
     * Updates the current instance of the AliasPortlet object
     */
    public void update(  )
    {
        AliasPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the AliasPortlet object
     */
    public void remove(  )
    {
        AliasPortletHome.getInstance(  ).remove( this );
    }
}
