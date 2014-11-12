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

import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.validation.ConstraintViolation;


/**
 * This class provides a service that build messages and deliver the Url to display them
 */
public final class AdminMessageService
{
    private static final String ATTRIBUTE_MESSAGE = "LUTECE_ADMIN_MESSAGE";
    private static final String JSP_ADMIN_MESSAGE = "jsp/admin/AdminMessage.jsp";
    private static final String JSP_BACK = "javascript:history.go(-1)";
    private static final String TARGET_SELF = "_self";
    private static final String PROPERTY_TITLE_DEFAULT = "portal.util.message.titleDefault";
    private static final String PROPERTY_TITLE_QUESTION = "portal.util.message.titleQuestion";
    private static final String PROPERTY_TITLE_ERROR = "portal.util.message.titleError";
    private static final String PROPERTY_TITLE_WARNING = "portal.util.message.titleWarning";
    private static final String PROPERTY_TITLE_CONFIRMATION = "portal.util.message.titleConfirmation";
    private static final String PROPERTY_TITLE_STOP = "portal.util.message.titleStop";
    private static final String TEMPLATE_FORMAT_LIST = "admin/util/message_list.html";
    private static final String MARK_MESSAGES_LIST = "messages_list";
    private static final String TEMPLATE_ERRORS_LIST = "admin/util/errors_list.html";
    private static final String MARK_ERRORS_LIST = "errors_list";

    /**
     * Private constructor
     */
    private AdminMessageService(  )
    {
    }

    /**
     * Returns the Url that display the given message
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @return The Url of the JSP that display the message
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey )
    {
        return getMessageUrl( request, strMessageKey, null, null, JSP_BACK, TARGET_SELF, AdminMessage.TYPE_INFO );
    }

    /**
     * Returns the Url that display the given message
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param nMessageType The message type
     * @return The Url of the JSP that display the message
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, int nMessageType )
    {
        return getMessageUrl( request, strMessageKey, null, null, JSP_BACK, TARGET_SELF, nMessageType );
    }

    /**
     * Returns the Url that display the given message
     * @return The Url of the JSP that display the message
     * @param messageArgs Message arguments
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param nMessageType The message type
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        int nMessageType )
    {
        return getMessageUrl( request, strMessageKey, messageArgs, null, JSP_BACK, TARGET_SELF, nMessageType );
    }

    /**
     * Returns the Url that display the given message
     * @return The Url of the JSP that display the message
     * @param strTarget Target
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param strUrl The Url of the OK button
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, String strUrl,
        String strTarget )
    {
        return getMessageUrl( request, strMessageKey, null, null, strUrl, strTarget, AdminMessage.TYPE_INFO );
    }

    /**
     * Returns the Url that display the given message
     * @return The Url of the JSP that display the message
     * @param strTarget The url target if not "_self"
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param strUrl The Url of the Ok button
     * @param nMessageType The message type
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, String strUrl,
        String strTarget, int nMessageType )
    {
        return getMessageUrl( request, strMessageKey, null, null, strUrl, strTarget, nMessageType );
    }

    /**
     * Returns the Url that display the given message
     * @return The Url of the JSP that display the message
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param strUrl The Url of the Ok button
     * @param nMessageType The message type
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, String strUrl,
        int nMessageType )
    {
        return getMessageUrl( request, strMessageKey, null, null, strUrl, "", nMessageType );
    }

    /**
     * Returns the Url that display the given message
     * @return The Url of the JSP that display the message
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param strUrl The Url of the Ok button
     * @param nMessageType The message type
     * @param requestParameters a collection of parameters
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, String strUrl,
        int nMessageType, Map requestParameters )
    {
        return getMessageUrl( request, strMessageKey, null, null, strUrl, "", nMessageType, requestParameters );
    }

    /**
     * Returns the Url that display the given message
     * @return The Url of the JSP that display the message
     * @param messageArgs Message Arguments
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param strUrl The Url of the Ok button
     * @param nMessageType The message type
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strUrl, int nMessageType )
    {
        return getMessageUrl( request, strMessageKey, messageArgs, null, strUrl, "", nMessageType );
    }

    /**
     * Returns the Url that display the given message
     * @return The Url of the JSP that display the message
     * @param messageArgs Message Arguments
     * @param strTarget The url target if not "_self"
     * @param strTitleKey The title key
     * @param nMessageType The message type
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param strUrl The Url of the Ok button
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strTitleKey, String strUrl, String strTarget, int nMessageType )
    {
        return getMessageUrl( request, strMessageKey, messageArgs, strTitleKey, strUrl, strTarget, nMessageType, null );
    }

    /**
     * Returns the Url that display the given message
     * @return The Url of the JSP that display the message
     * @param messageArgs Message Arguments
     * @param strTarget The url target if not "_self"
     * @param strTitleKey The title key
     * @param nMessageType The message type
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param strUrl The Url of the Ok button
     * @param requestParameters a collection of parameters
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strTitleKey, String strUrl, String strTarget, int nMessageType, Map<String, Object> requestParameters )
    {
        return getMessageUrl( request, strMessageKey, messageArgs, strTitleKey, strUrl, strTarget, nMessageType,
            requestParameters, null );
    }

    /**
     * Returns the Url that display the given message
     * @param <T> The type of the bean that has been validated
     * @param request The HTTP request
     * @param strMessageKey The message key
     * @param constraintViolations The set of violations
     * @return The Url of the JSP that display the message
     */
    public static <T> String getMessageUrl( HttpServletRequest request, String strMessageKey,
        Set<ConstraintViolation<T>> constraintViolations )
    {
        return getMessageUrl( request, strMessageKey, formatConstraintViolations( request, constraintViolations ),
            null, JSP_BACK, TARGET_SELF, AdminMessage.TYPE_ERROR );
    }

    /**
     * Returns the Url that display the given message
     * @param request The HTTP request
     * @param strMessageKey The message key
     * @param errors The set of violations
     * @return The Url of the JSP that display the message
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey,
        List<?extends ErrorMessage> errors )
    {
        return getMessageUrl( request, strMessageKey, formatValidationErrors( request, errors ), null, JSP_BACK,
            TARGET_SELF, AdminMessage.TYPE_ERROR );
    }

    /**
     * Returns the Url that display the given message.
     *
     * @param request The HttpRequest
     * @param strMessageKey The message key
     * @param messageArgs Message Arguments
     * @param strTitleKey The title key
     * @param strUrl The Url of the Ok button
     * @param strTarget The url target if not "_self"
     * @param nMessageType The message type
     * @param requestParameters a collection of parameters
     * @param strBackUrl the str back url
     * @return The Url of the JSP that display the message
     */
    public static String getMessageUrl( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strTitleKey, String strUrl, String strTarget, int nMessageType, Map<String, Object> requestParameters,
        String strBackUrl )
    {
        String strTitle = ( strTitleKey != null ) ? strTitleKey : getDefaultTitle( nMessageType );
        boolean bCancelButton = getCancelButton( nMessageType );
        AdminMessage message = new AdminMessage( strMessageKey, messageArgs, strTitle, strUrl, strTarget, nMessageType,
                bCancelButton, requestParameters, strBackUrl );
        setMessage( request, message );

        return getUrl( request );
    }

    /**
     * Returns the message associated to the current request
     * @param request The HttpRequest
     * @return The message associated to the current request
     */
    public static AdminMessage getMessage( HttpServletRequest request )
    {
        HttpSession session = request.getSession( true );
        AdminMessage message = (AdminMessage) session.getAttribute( ATTRIBUTE_MESSAGE );

        return message;
    }

    /**
     * Return relative url for the admin message jsp.
     * This method does not generate admin message.
     * @return The relative url
     */
    public static String getMessageRelativeUrl(  )
    {
        return JSP_ADMIN_MESSAGE;
    }

    /**
     * Store a message into the current session
     * @param request The HTTP request
     * @param message The message to store
     */
    private static void setMessage( HttpServletRequest request, AdminMessage message )
    {
        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_MESSAGE, message );
    }

    /**
     * Build the message url
     * @param request The HTTP request
     * @return The Url
     */
    private static String getUrl( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + JSP_ADMIN_MESSAGE;
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
            case AdminMessage.TYPE_QUESTION:
                strTitleKey = PROPERTY_TITLE_QUESTION;

                break;

            case AdminMessage.TYPE_ERROR:
                strTitleKey = PROPERTY_TITLE_ERROR;

                break;

            case AdminMessage.TYPE_WARNING:
                strTitleKey = PROPERTY_TITLE_WARNING;

                break;

            case AdminMessage.TYPE_CONFIRMATION:
                strTitleKey = PROPERTY_TITLE_CONFIRMATION;

                break;

            case AdminMessage.TYPE_STOP:
                strTitleKey = PROPERTY_TITLE_STOP;

                break;

            default:
                strTitleKey = PROPERTY_TITLE_DEFAULT;

                break;
        }

        return strTitleKey;
    }

    /**
     * Returns if the cancel button should be displayed or not according the message type
     * @param nMessageType The message type
     * @return True if the button should be displayed, otherwise false
     */
    private static boolean getCancelButton( int nMessageType )
    {
        boolean bCancel;

        switch ( nMessageType )
        {
            case AdminMessage.TYPE_QUESTION:
            case AdminMessage.TYPE_CONFIRMATION:
                bCancel = true;

                break;

            default:
                bCancel = false;

                break;
        }

        return bCancel;
    }

    /**
     * Format a list of item to include in a message
     * @param list The list of item as string
     * @param locale The current locale
     * @return The formatted list
     */
    public static String getFormattedList( List<String> list, Locale locale )
    {
        Map<String, List<String>> model = new HashMap<String, List<String>>(  );
        model.put( MARK_MESSAGES_LIST, list );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FORMAT_LIST, locale, model );

        return template.getHtml(  );
    }

    /**
     * Format a set of constraints violations as en error list.
     * @param <T> The type of the object
     * @param request The HTTP request
     * @param constraintViolations The set of violations
     * @return The formatted errors list as an object array
     */
    private static <T> Object[] formatConstraintViolations( HttpServletRequest request,
        Set<ConstraintViolation<T>> constraintViolations )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ERRORS_LIST, constraintViolations );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ERRORS_LIST, request.getLocale(  ), model );
        String[] formatedErrors = { template.getHtml(  ) };

        return formatedErrors;
    }

    /**
     * Format a set of constraints violations as en error list.
     * @param <T> The type of the bean that has been validated
     * @param request The HTTP request
     * @param errors The set of violations
     * @return The formatted errors list as an object array
     */
    private static <T> Object[] formatValidationErrors( HttpServletRequest request, List<?extends ErrorMessage> errors )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ERRORS_LIST, errors );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ERRORS_LIST, request.getLocale(  ), model );
        String[] formatedErrors = { template.getHtml(  ) };

        return formatedErrors;
    }
}
