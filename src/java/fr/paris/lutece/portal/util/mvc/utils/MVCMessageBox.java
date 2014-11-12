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
package fr.paris.lutece.portal.util.mvc.utils;

import fr.paris.lutece.portal.service.i18n.I18nService;

import java.util.Locale;


/**
 * MVCMessageBox
 */
public class MVCMessageBox
{
    public static final int INFO = 0;
    public static final int QUESTION = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    private int _nStyle;
    private String _strTitle;
    private String _strMessage;
    private String _strLabelButton1;
    private String _strUrlButton1;
    private String _strLabelKeyButton1;
    private String _strLabelButton2;
    private String _strUrlButton2;
    private String _strLabelKeyButton2;
    private String _strTemplate;
    private String _strTitleKey;
    private String _strMessageKey;

    /**
     * @return the style
     */
    public int getStyle(  )
    {
        return _nStyle;
    }

    /**
     * @param nStyle the style to set
     */
    public void setStyle( int nStyle )
    {
        _nStyle = nStyle;
    }

    /**
     * @return the title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * @param strTitle the title to set
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * @return the message
     */
    public String getMessage(  )
    {
        return _strMessage;
    }

    /**
     * @param strMessage the message to set
     */
    public void setMessage( String strMessage )
    {
        _strMessage = strMessage;
    }

    /**
     * @return the strLabelButton1
     */
    public String getLabelButton1(  )
    {
        return _strLabelButton1;
    }

    /**
     * @param strLabelButton1 the strLabelButton1 to set
     */
    public void setLabelButton1( String strLabelButton1 )
    {
        _strLabelButton1 = strLabelButton1;
    }

    /**
     * @return the  strUrlButton1
     */
    public String getUrlButton1(  )
    {
        return _strUrlButton1;
    }

    /**
     * @param  strUrlButton1 the  strUrlButton1 to set
     */
    public void setUrlButton1( String strUrlButton1 )
    {
        _strUrlButton1 = strUrlButton1;
    }

    /**
     * @return the label
     */
    public String getLabelButton2(  )
    {
        return _strLabelButton2;
    }

    /**
     * @param strLabelButton2 the label to set
     */
    public void setLabelButton2( String strLabelButton2 )
    {
        _strLabelButton2 = strLabelButton2;
    }

    /**
     * @return the  strUrlButton2
     */
    public String getUrlButton2(  )
    {
        return _strUrlButton2;
    }

    /**
     * @param  strUrlButton2 the  strUrlButton2 to set
     */
    public void setUrlButton2( String strUrlButton2 )
    {
        _strUrlButton2 = strUrlButton2;
    }

    /**
     * @return the _strLabelKeyButton1
     */
    public String getLabelKeyButton1(  )
    {
        return _strLabelKeyButton1;
    }

    /**
     * @param strLabelKeyButton1 the _strLabelKeyButton1 to set
     */
    public void setLabelKeyButton1( String strLabelKeyButton1 )
    {
        _strLabelKeyButton1 = strLabelKeyButton1;
    }

    /**
     * @return the _strLabelKeyButton2
     */
    public String getLabelKeyButton2(  )
    {
        return _strLabelKeyButton2;
    }

    /**
     * @param strLabelKeyButton2 the _strLabelKeyButton2 to set
     */
    public void setLabelKeyButton2( String strLabelKeyButton2 )
    {
        _strLabelKeyButton2 = strLabelKeyButton2;
    }

    /**
     * @return the _strTemplate
     */
    public String getTemplate(  )
    {
        return _strTemplate;
    }

    /**
     * @param strTemplate the _strTemplate to set
     */
    public void setTemplate( String strTemplate )
    {
        _strTemplate = strTemplate;
    }

    /**
     * @return the Title key
     */
    public String getTitleKey(  )
    {
        return _strTitleKey;
    }

    /**
     * @param strTitleKey the Title key to set
     */
    public void setTitleKey( String strTitleKey )
    {
        this._strTitleKey = strTitleKey;
    }

    /**
     * @return the Message key
     */
    public String getMessageKey(  )
    {
        return _strMessageKey;
    }

    /**
     * @param strMessageKey the Message key to set
     */
    public void setMessageKey( String strMessageKey )
    {
        this._strMessageKey = strMessageKey;
    }

    /**
     * Localize value if value's key found
     * @param locale The locale
     */
    public void localize( Locale locale )
    {
        _strTitle = ( _strTitleKey != null ) ? I18nService.getLocalizedString( _strTitleKey, locale ) : _strTitle;
        _strMessage = ( _strMessageKey != null ) ? I18nService.getLocalizedString( _strMessageKey, locale ) : _strMessage;
        _strLabelButton1 = ( _strLabelKeyButton1 != null )
            ? I18nService.getLocalizedString( _strLabelKeyButton1, locale ) : _strLabelButton1;
        _strLabelButton2 = ( _strLabelKeyButton2 != null )
            ? I18nService.getLocalizedString( _strLabelKeyButton2, locale ) : _strLabelButton2;
    }
}
