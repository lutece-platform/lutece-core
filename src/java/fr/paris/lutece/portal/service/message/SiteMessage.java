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

import fr.paris.lutece.portal.service.i18n.I18nService;

import java.io.Serializable;

import java.text.MessageFormat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;


/**
 * The class provides a bean to hold message box informations
 */
public class SiteMessage implements Serializable
{
    public static final int TYPE_INFO = 0;
    public static final int TYPE_QUESTION = 1;
    public static final int TYPE_ERROR = 2;
    public static final int TYPE_WARNING = 3;
    public static final int TYPE_CONFIRMATION = 4;
    public static final int TYPE_STOP = 5;
    public static final int TYPE_BUTTON_HIDDEN = 0;
    public static final int TYPE_BUTTON_BACK = 1;
    public static final int TYPE_BUTTON_CANCEL = 2;
    private static final long serialVersionUID = -34775038853250525L;
    private String _strTextKey;
    private String _strTitleKey;
    private String _strUrl;
    private String _strTarget;
    private int _nTypeButton;
    private int _nType;
    private String[] _messageArgs;
    private Map<String, String> _requestParameters;
    private String _strBackUrl;

    /**
    *
    * @param strTextKey I18n key for the message body
    * @param messageArgs Arguments for the strTextKey or null
    * @param strTitleKey I18n key for the message title
    * @param strUrl The url for the Ok button
    * @param strTarget Target for the form (_blank, _self, ...)
    * @param nType Message type (TYPE_INFO, TYPE_QUESTION, ...)
    * @param nTypeButton Type of Cancel button
    * @param requestParameters Request parameters as a Map
    * @param strBackUrl the back url
    */
    public SiteMessage( String strTextKey, Object[] messageArgs, String strTitleKey, String strUrl, String strTarget,
        int nType, int nTypeButton, Map<String, Object> requestParameters, String strBackUrl )
    {
        _strTextKey = strTextKey;
        _strTitleKey = strTitleKey;
        _strUrl = strUrl;
        _strTarget = strTarget;
        _nType = nType;
        _nTypeButton = nTypeButton;
        _strBackUrl = strBackUrl;

        // Object message conversion into String values
        if ( messageArgs != null )
        {
            _messageArgs = new String[messageArgs.length];

            for ( int i = 0; i < messageArgs.length; i++ )
            {
                _messageArgs[i] = ( messageArgs[i] == null ) ? null : messageArgs[i].toString(  );
            }
        }

        // Object message conversion into String values for map of parameters
        if ( requestParameters != null )
        {
            _requestParameters = new HashMap<String, String>(  );

            for ( Entry<String, Object> entry : requestParameters.entrySet(  ) )
            {
                _requestParameters.put( entry.getKey(  ),
                    ( entry.getValue(  ) == null ) ? null : entry.getValue(  ).toString(  ) );
            }
        }
    }

    /**
    *
    * @param strTextKey I18n key for the message body
    * @param messageArgs Arguments for the strTextKey or null
    * @param strTitleKey I18n key for the message title
    * @param strUrl The url for the Ok button
    * @param strTarget Target for the form (_blank, _self, ...)
    * @param nType Message type (TYPE_INFO, TYPE_QUESTION, ...)
    * @param bCancelButton True if Cancel button is necessary
    * @deprecated
    */
    public SiteMessage( String strTextKey, Object[] messageArgs, String strTitleKey, String strUrl, String strTarget,
        int nType, boolean bCancelButton )
    {
        this( strTextKey, messageArgs, strTitleKey, strUrl, strTarget, nType, ( bCancelButton ) ? 1 : 0, null, null );
    }

    /**
     * Get the type of message
     *
     * @return The message type
     */
    public int getType(  )
    {
        return _nType;
    }

    /**
     * return True if button is required
     * @return True if button is required in message
     * @deprecated
     */
    public boolean isCancel(  )
    {
        return ( getTypeButton(  ) == TYPE_BUTTON_HIDDEN ) ? false : true;
    }

    /**
     * set cancel button value
     *
     * @param bCancel True if Cancel button is required (set with default type)
     * @deprecated
     */
    public void setCancel( boolean bCancel )
    {
        if ( !bCancel )
        {
            setTypeButton( TYPE_BUTTON_HIDDEN );
        }
        else
        {
            setTypeButton( TYPE_BUTTON_BACK );
        }
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
            strText = MessageFormat.format( strText, (Object[]) _messageArgs );
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
     *
     * @return type of button
     */
    public int getTypeButton(  )
    {
        return _nTypeButton;
    }

    /**
     *
     * @param nTypeButton The Type of cancel button
     */
    public void setTypeButton( int nTypeButton )
    {
        _nTypeButton = nTypeButton;
    }

    /**
     *
     * @return the request parameters.
     */
    public Map<String, String> getRequestParameters(  )
    {
        return _requestParameters;
    }

    /**
     * set the back url
     * @param strBackUrl the back url
     */
    public void setBackUrl( String strBackUrl )
    {
        this._strBackUrl = strBackUrl;
    }

    /**
     *
     * @return back url
     */
    public String getBackUrl(  )
    {
        return _strBackUrl;
    }
}
