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
package fr.paris.lutece.portal.web.xpages;

import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.xml.XmlUtil;


/**
 * This class represents XPage object
 *
 */
public class XPage
{
    private static final String TAG_PAGE_LINK = "page_link";
    private static final String TAG_PAGE_NAME = "page-name";
    private static final String TAG_PAGE_URL = "page-url";
    private String _strContent;
    private String _strTitle;
    private String _strKeyword;
    private String _strPathLabel;
    private String _strXmlExtendedPathLabel;
    private boolean _bStandalone;

    /**
     *
     * @return The content of the page
     */
    public String getContent(  )
    {
        return _strContent;
    }

    /**
     *
     * @return The keyword of the page
     */
    public String getKeyword(  )
    {
        return _strKeyword;
    }

    /**
     *
     * @return The title of the page
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     *
     * @return The path label of the page
     */
    public String getPathLabel(  )
    {
        return _strPathLabel;
    }

    /**
     *
     * @param strContent The content of the page
     */
    public void setContent( String strContent )
    {
        _strContent = strContent;
    }

    /**
     *
     * @param strKeyword The keyword of the page
     */
    public void setKeyword( String strKeyword )
    {
        _strKeyword = strKeyword;
    }

    /**
     *
     * @param strTitle The title of the page
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     *
     * @param strPathLabel The path label of the page
     */
    public void setPathLabel( String strPathLabel )
    {
        _strPathLabel = strPathLabel;
    }

    /**
     * Get the extended path label, which is given as a Xml code
     * @return Returns the Extended Path Label
     */
    public String getXmlExtendedPathLabel(  )
    {
        return _strXmlExtendedPathLabel;
    }

    /**
     * Set the Extended Path Label from a xml string
     * @param strXmlExtendedPathLabel the Extended Path Label to set
     */
    public void setXmlExtendedPathLabel( String strXmlExtendedPathLabel )
    {
        _strXmlExtendedPathLabel = strXmlExtendedPathLabel;
    }

    /**
     * Build a path from a referencelist. Each item of the list is an element of the path
     * The item's code is the label, the item's name is used for the URL of the link.
     * @param listPathItem The items of the path.
     */
    public void setExtendedPathLabel( ReferenceList listPathItem )
    {
        StringBuffer sbXml = new StringBuffer(  );

        for ( ReferenceItem item : listPathItem )
        {
            XmlUtil.beginElement( sbXml, TAG_PAGE_LINK );
            XmlUtil.addElement( sbXml, TAG_PAGE_NAME, item.getCode(  ) );
            XmlUtil.addElement( sbXml, TAG_PAGE_URL, item.getName(  ) );
            XmlUtil.endElement( sbXml, TAG_PAGE_LINK );
        }

        _strXmlExtendedPathLabel = sbXml.toString(  );
    }

    /**
     * Indicates if the content is standalone and should not be encapsulated in other
     * content, such as portal headers and footers
     * @return <code>true</code> is the content is standalone
     * @since 5.1.0
     */
    public boolean isStandalone(  )
    {
        return _bStandalone;
    }

    /**
     * Indicates if the content is standalone and should not be encapsulated in other
     * content, such as portal headers and footers
     * @param standalone <code>true</code> is the content is standalone
     * @since 5.1.0
     */
    public void setStandalone( boolean standalone )
    {
        _bStandalone = standalone;
    }
}
