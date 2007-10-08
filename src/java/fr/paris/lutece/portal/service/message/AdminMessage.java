/*
 * Copyright (c) 2002-2007, Mairie de Paris
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

import fr.paris.lutece.portal.service.i18n.I18nService;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.Map;


/**
 * The class provides a bean to hold message box informations
 */
public class AdminMessage
{
    public static final int TYPE_INFO = 0;
    public static final int TYPE_QUESTION = 1;
    public static final int TYPE_ERROR = 2;
    public static final int TYPE_WARNING = 3;
    public static final int TYPE_CONFIRMATION = 4;
    public static final int TYPE_STOP = 5;
    private String _strTextKey;
    private String _strTitleKey;
    private String _strUrl;
    private String _strTarget;
    private int _nType;
    private boolean _bCancel;
    private Object[] _messageArgs;
    private Map _requestParameters;
    
    /** Creates a new instance of AppMessage */
    public AdminMessage( String strTextKey, Object[] messageArgs, String strTitleKey, String strUrl, String strTarget,
        int nType, boolean bCancelButton )
    {
    	buildAdminMessage ( strTextKey, messageArgs, strTitleKey, strUrl, strTarget, nType, bCancelButton, null );
    }
    
    /** Creates a new instance of AppMessage with request parameters */
    public AdminMessage( String strTextKey, Object[] messageArgs, String strTitleKey, String strUrl, String strTarget,
        int nType, boolean bCancelButton, Map requestParameters )
    {
    	buildAdminMessage ( strTextKey, messageArgs, strTitleKey, strUrl, strTarget, nType, bCancelButton, requestParameters );
    }
    
    /**
     * Build a new admin message
     * 
     * @param strTextKey the text key
     * @param messageArgs the message args
     * @param strTitleKey the title key
     * @param strUrl the URL
     * @param strTarget the target
     * @param nType the type
     * @param bCancelButton the cancel button
     * @param requestParameters the request parameters
     */
    private void buildAdminMessage ( String strTextKey, Object[] messageArgs, String strTitleKey, String strUrl, String strTarget,
            int nType, boolean bCancelButton, Map requestParameters )
    {
    	_strTextKey = strTextKey;
        _strTitleKey = strTitleKey;
        _strUrl = strUrl;
        _strTarget = strTarget;
        _nType = nType;
        _bCancel = bCancelButton;
        _messageArgs = messageArgs;
        _requestParameters = requestParameters;
    }
    
    /**
     * Return the type of message
     * @return the type message
     */
    public int getType(  )
    {
        return _nType;
    }

    /**
     * Return if the cancel button is display
     * @return true if the cancel button is display
     */
    public boolean isCancel(  )
    {
        return _bCancel;
    }
    
    /**
     * Set the display of cancel button
     * @param bCancel the new bCancel
     */
    public void setCancel( boolean bCancel )
    {
        _bCancel = bCancel;
    }

    /**
     * Returns the localized text of the message
     * @param locale The current locale
     * @return The localized text of the message
     */
    public String getText( Locale locale )
    {
        String strText = I18nService.getLocalizedString( _strTextKey, locale );

        if ( _messageArgs != null )
        {
            strText = MessageFormat.format( strText, _messageArgs );
        }

        return strText;
    }

    /**
     * Returns the localized text of the message
     * @param locale The current locale
     * @return The localized text of the message
     */
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( _strTitleKey, locale );
    }

    /**
     * Returns the Url of the message box Ok button
     * @return the Url of the Ok button
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Returns the Url of the message box Ok button
     * @return the Url of the Ok button
     */
    public String getTarget(  )
    {
        return _strTarget;
    }
    
    /**
     * Return the request parameters 
     * @return the request parameters
     */
    public Map getRequestParameters( )
    {
    	return _requestParameters;
    }
}
