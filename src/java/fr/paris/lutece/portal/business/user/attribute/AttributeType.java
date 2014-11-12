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
package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.service.i18n.I18nService;

import java.util.Locale;


/**
 *
 * AttributeType
 *
 */
public class AttributeType
{
    private String _strLabelType;
    private String _strClassName;
    private Locale _locale;

    /**
     * Get label type
     * @return label type
     */
    public String getLabelType(  )
    {
        return _strLabelType;
    }

    /**
     * Set label type
     * @param strLabelType label type
     */
    public void setLabelType( String strLabelType )
    {
        _strLabelType = strLabelType;
    }

    /**
     * Get the label type.
     *
     * @return the name
     */
    public String getName(  )
    {
        return I18nService.getLocalizedString( _strLabelType, _locale );
    }

    /**
     * Get class name
     * @return class name
     */
    public String getClassName(  )
    {
        return _strClassName;
    }

    /**
     * Set class name
     * @param strClassName class name
     */
    public void setClassName( String strClassName )
    {
        _strClassName = strClassName;
    }

    /**
     * Set locale
     * @param locale Locale
     */
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * Get locale
     * @return Locale
     */
    public Locale getLocale(  )
    {
        return _locale;
    }
}
