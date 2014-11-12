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
package fr.paris.lutece.portal.service.message;

import fr.paris.lutece.util.url.UrlItem;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class provides a service that build messages
 */
public final class SiteMessageService
{
    private static final String ATTRIBUTE_MESSAGE = "LUTECE_PORTAL_MESSAGE";
    private static final String PROPERTY_TITLE_DEFAULT = "portal.util.message.titleDefault";
    private static final String PROPERTY_TITLE_QUESTION = "portal.util.message.titleQuestion";
    private static final String PROPERTY_TITLE_ERROR = "portal.util.message.titleError";
    private static final String PROPERTY_TITLE_WARNING = "portal.util.message.titleWarning";
    private static final String PROPERTY_TITLE_CONFIRMATION = "portal.util.message.titleConfirmation";
    private static final String PROPERTY_TITLE_STOP = "portal.util.message.titleStop";
    private static final String PARAMETER_SITE_MESSAGE = "sitemessage";
    private static final String PARAMETER_SITE_MESSAGE_VALUE = "true";

    /**
     * Private constructor
     */
    private SiteMessageService(  )
    {
    }

    /**
     * Set the INFO message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey )
        throws SiteMessageException
    {
        setMessage( request, strMessageKey, null, null, null, null, SiteMessage.TYPE_INFO );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param nMessageType The message type
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, int nMessageType )
        throws SiteMessageException
    {
        setMessage( request, strMessageKey, null, null, null, null, nMessageType );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param nMessageType The message type
     * @param strUrl The url of the Ok button
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, int nMessageType, String strUrl )
        throws SiteMessageException
    {
        setMessage( request, strMessageKey, null, null, strUrl, null, nMessageType );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param nMessageType The message type
     * @param strUrl The url of the Ok button
     * @param requestParameters The request parameters as a map
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, int nMessageType, String strUrl,
        Map<String, Object> requestParameters ) throws SiteMessageException
    {
        setMessage( request, strMessageKey, null, null, strUrl, null, nMessageType, requestParameters );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param strTitleKey The title key
     * @param nMessageType The message type
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, String strTitleKey,
        int nMessageType ) throws SiteMessageException
    {
        setMessage( request, strMessageKey, null, strTitleKey, null, null, nMessageType );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param messageArgs Message Arguments
     * @param strTitleKey The title key
     * @param nMessageType The message type
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strTitleKey, int nMessageType ) throws SiteMessageException
    {
        setMessage( request, strMessageKey, messageArgs, strTitleKey, null, null, nMessageType );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param messageArgs Message Arguments
     * @param nMessageType The message type
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        int nMessageType ) throws SiteMessageException
    {
        setMessage( request, strMessageKey, messageArgs, null, null, null, nMessageType );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param messageArgs Message arguments
     * @param strTitleKey The title key
     * @param nMessageType The message type
     * @param strUrl The URL
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        int nMessageType, String strUrl, String strTitleKey )
        throws SiteMessageException
    {
        setMessage( request, strMessageKey, messageArgs, strTitleKey, strUrl, null, nMessageType );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param messageArgs Message Arguments
     * @param strUrl The forward URL
     * @param strTitleKey The title key
     * @param nMessageType The message type
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, int nMessageType, String strUrl,
        String strTitleKey, Object[] messageArgs ) throws SiteMessageException
    {
        setMessage( request, strMessageKey, messageArgs, strTitleKey, strUrl, null, nMessageType );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param messageArgs Message Arguments
     * @param strTitleKey The title key
     * @param strUrl The Url of the Ok button
     * @param strTarget The url target if not "_self"
     * @param nMessageType The message type
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strTitleKey, String strUrl, String strTarget, int nMessageType )
        throws SiteMessageException
    {
        setMessage( request, strMessageKey, messageArgs, strTitleKey, strUrl, strTarget, nMessageType, null );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param messageArgs Message Arguments
     * @param strTitleKey The title key
     * @param strUrl The Url of the Ok button
     * @param strTarget The url target if not "_self"
     * @param nMessageType The message type
     * @param requestParameters The request parameters
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strTitleKey, String strUrl, String strTarget, int nMessageType, Map<String, Object> requestParameters )
        throws SiteMessageException
    {
        setMessage( request, strMessageKey, messageArgs, strTitleKey, strUrl, strTarget, nMessageType,
            requestParameters, null );
    }

    /**
     * Set the message, store it in session and throw a
     * LuteceSiteMessageException
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param messageArgs Message Arguments
     * @param strTitleKey The title key
     * @param strUrl The Url of the Ok button
     * @param strTarget The url target if not "_self"
     * @param nMessageType The message type
     * @param requestParameters The request parameters
     * @param strBackUrl The Url of back button
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static void setMessage( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strTitleKey, String strUrl, String strTarget, int nMessageType, Map<String, Object> requestParameters,
        String strBackUrl ) throws SiteMessageException
    {
        String strTitle = ( strTitleKey != null ) ? strTitleKey : getDefaultTitle( nMessageType );
        SiteMessage message = new SiteMessage( strMessageKey, messageArgs, strTitle, strUrl, strTarget, nMessageType,
                getTypeButton( nMessageType, strUrl ), requestParameters, strBackUrl );
        setMessage( request, message );

        throw new SiteMessageException(  );
    }

    /**
     * Returns the message associated to the current request
     * @param request The HttpRequest
     * @return The message associated to the current request
     */
    public static SiteMessage getMessage( HttpServletRequest request )
    {
        HttpSession session = request.getSession( true );
        SiteMessage message = (SiteMessage) session.getAttribute( ATTRIBUTE_MESSAGE );

        return message;
    }

    /**
     * Store a message into the current session
     * @param request The HTTP request
     * @param message The message to store
     */
    private static void setMessage( HttpServletRequest request, SiteMessage message )
    {
        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_MESSAGE, message );
    }

    /**
     * Delete the message in session
     *
     * @param request The HTTP request
     */
    public static void cleanMessageSession( HttpServletRequest request )
    {
        HttpSession session = request.getSession( true );
        session.removeAttribute( ATTRIBUTE_MESSAGE );
    }

    /**
     * Set the site message url with parameters if necessary
     *
     * @param strRequestUrl The Request url
     * @return The message url
     */
    public static String setSiteMessageUrl( String strRequestUrl )
    {
        UrlItem urlItem = new UrlItem( strRequestUrl );
        urlItem.addParameter( PARAMETER_SITE_MESSAGE, PARAMETER_SITE_MESSAGE_VALUE );

        return urlItem.getUrl(  );
    }

    /**
     * Returns a default title for the message box
     * @param nMessageType The message type
     * @return The default title
     */
    private static String getDefaultTitle( int nMessageType )
    {
        String strTitleKey;

        switch ( nMessageType )
        {
            case SiteMessage.TYPE_QUESTION:
                strTitleKey = PROPERTY_TITLE_QUESTION;

                break;

            case SiteMessage.TYPE_ERROR:
                strTitleKey = PROPERTY_TITLE_ERROR;

                break;

            case SiteMessage.TYPE_WARNING:
                strTitleKey = PROPERTY_TITLE_WARNING;

                break;

            case SiteMessage.TYPE_CONFIRMATION:
                strTitleKey = PROPERTY_TITLE_CONFIRMATION;

                break;

            case SiteMessage.TYPE_STOP:
                strTitleKey = PROPERTY_TITLE_STOP;

                break;

            default:
                strTitleKey = PROPERTY_TITLE_DEFAULT;

                break;
        }

        return strTitleKey;
    }

    /**
     * Returns if the cancel button should be displayed or not according the
     * message type
     * @param nMessageType The message type
     * @param strUrl The url of the Ok button
     * @return the type of button
     */
    private static int getTypeButton( int nMessageType, String strUrl )
    {
        /*
         * ------------------------------------- *
         * Type url defined no url *
         * TYPE_INFO none back *
         * TYPE_QUESTION cancel back(?) *
         * TYPE_ERROR none back *
         * TYPE_WARNING none back *
         * TYPE_CONFIRMATION cancel back *
         * TYPE_STOP none back *
         * -------------------------------------
         */
        int nTypeButton;

        if ( ( strUrl != null ) && !strUrl.equals( "" ) )
        {
            switch ( nMessageType )
            {
                case SiteMessage.TYPE_QUESTION:
                case SiteMessage.TYPE_CONFIRMATION:
                    nTypeButton = SiteMessage.TYPE_BUTTON_CANCEL;

                    break;

                default:
                    nTypeButton = SiteMessage.TYPE_BUTTON_HIDDEN;

                    break;
            }
        }
        else
        {
            nTypeButton = SiteMessage.TYPE_BUTTON_BACK;
        }

        return nTypeButton;
    }
}
