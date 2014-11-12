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
package fr.paris.lutece.portal.business.rss;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *  IResourceRss
 */
public interface IResourceRss
{
    /**
     *
     * @return the rss Id
     */
    int getId(  );

    /**
     * set the rss id
     * @param nId the rss id
     */
    void setId( int nId );

    /**
    *
    * @return the description
    */
    String getDescription(  );

    /**
     * set the description
     * @param strDescription the description
     */
    void setDescription( String strDescription );

    /**
    *
    * @return the name
    */
    String getName(  );

    /**
     * set the name
     * @param strName the name
     */
    void setName( String strName );

    /**
     *
     * @return the ResourceRssType Object
     */
    IResourceRssType getResourceRssType(  );

    /**
     * set the ResourceRssType object
     * @param resourceRssType the ResourceRsstype object
     */
    void setResourceRssType( IResourceRssType resourceRssType );

    /**
     * validates the user input associated to the resource rss
     * @param request request
     * @param locale locale
     * @return null if there is no error in the resourceRss form
     *                    else return the error message url
     */
    String doValidateConfigForm( HttpServletRequest request, Locale locale );

    /**
     * returns the informations which must be displayed in the creation resourceRss configuration
     * @param request request
     * @param locale locale
     * @return the information which must  be displayed in the creation resourceRss configuration
     */
    String getDisplayCreateConfigForm( HttpServletRequest request, Locale locale );

    /**
     * returns the informations which must  be displayed in the modification resourceRss configuration
     * @param request request
     * @param locale locale
     * @return the information which must  be displayed in the modification resourceRss configuration
     */
    String getDisplayModifyConfigForm( HttpServletRequest request, Locale locale );

    /**
     * Save the ResourceRss configuration
     * @param request request
     * @param locale locale
     */
    void doSaveConfig( HttpServletRequest request, Locale locale );

    /**
     * Update the ResourceRss configuration
     * @param request request
     * @param locale locale
     */
    void doUpdateConfig( HttpServletRequest request, Locale locale );

    /**
     * verified that the resource contains the resource to be exploited
     * @return true if resourceRss content resource
     */
    boolean contentResourceRss(  );

    /**
     * create Html Rss
     * @return Html rss
     * @deprecated use {@link #getFeed()} instead.
     */
    @Deprecated
    String createHtmlRss(  );

    /**
     * Gets the feed with items
     * @return the {@link IFeedResource}
     */
    IFeedResource getFeed(  );

    /**
     * Update the ResourceRss configuration
     * @param idResourceRss id of resourceRss
     */
    void deleteResourceRssConfig( int idResourceRss );

    /**
     * Return the list of parameter to keep when action apply
     * @param request request
     * @return the map of parameter
     */
    Map<String, String> getParameterToApply( HttpServletRequest request );

    /**
     * verify that the resource exist
     * @return true if resource exist
     */
    boolean checkResource(  );

    /**
     * Gets the feed type
     * @return the feed type
     */
    String getFeedType(  );

    /**
     * Sets the feed type
     * @param strFeedType the feed type
     */
    void setFeedType( String strFeedType );

    /**
     * Gets the encoding
     * @return the encoding
     */
    String getEncoding(  );

    /**
     * Sets the encoding
     * @param strEncoding the encoding
     */
    void setEncoding( String strEncoding );

    /**
     * The number of displayed item.
     * @return the number of displayed item
     */
    int getMaxItems(  );

    /**
     * Sets the number of displayed item
     * @param nMaxItems number of displayed item
     */
    void setMaxItems( int nMaxItems );
}
