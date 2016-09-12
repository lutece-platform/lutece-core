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
package fr.paris.lutece.portal.business.right;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.i18n.Localizable;

import java.io.Serializable;

import java.util.Locale;


/**
 * This class represents business objects right
 */
public class Right implements Localizable, Comparable<Right>, Serializable
{
    private static final long serialVersionUID = 4075896005615205007L;

    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String EMPTY_STRING = "";
    private String _strId;
    private String _strNameKey;
    private String _strDescriptionKey;
    private int _nLevel;
    private String _strUrl;
    private String _strPluginName;
    private String _strFeatureGroup;
    private String _strIconUrl;
    private String _strDocumentationUrl;
    private Locale _locale;
    private int _nIdOrder;
    private boolean _bIsExternalFeature;

    /**
     * Set the local used by this right
     * @param locale the local to use
     */
    @Override
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * Returns the identifier of this right
     *
     * @return the identifier of this right
     */
    public String getId(  )
    {
        return _strId;
    }

    /**
     * Sets the identifier of the right to the specified string.
     *
     * @param strId the new identifier
     */
    public void setId( String strId )
    {
        _strId = strId;
    }

    /**
     * Returns the name of this right.
     *
     * @return the right name
     */
    public String getNameKey(  )
    {
        return _strNameKey;
    }

    /**
     * Returns the name of this right if the i18n key exists; else return the i18n key.
     *
     * @return the right name
     */
    public String getName(  )
    {
        String strReturn = I18nService.getLocalizedString( _strNameKey, _locale );
        if ( !strReturn.equals( "" ) )
        {
            return I18nService.getLocalizedString( _strNameKey, _locale );
        }
        else return _strNameKey;
    }

    /**
     * Sets the name of the right to the specified string.
     *
     * @param strNameKey the new name
     */
    public void setNameKey( String strNameKey )
    {
        _strNameKey = ( strNameKey == null ) ? EMPTY_STRING : strNameKey;
    }

    /**
     * Returns the level of this right.
     *
     * @return the right level
     */
    public int getLevel(  )
    {
        return _nLevel;
    }

    /**
     * Sets the level of the right to the specified int.
     *
     * @param nLevel the new level
     */
    public void setLevel( int nLevel )
    {
        _nLevel = nLevel;
    }

    /**
     * Returns the url of the jsp component which manages this right.
     *
     * @return the right url function
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Sets the url of the right to the specified string.
     *
     * @param strUrl the new url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }

    /**
     * Returns the description of this right.
     *
     * @return the right description
     */
    public String getDescriptionKey(  )
    {
        return _strDescriptionKey;
    }

    /**
     * Returns the description of this right if the i18nk exists; else return the key.
     *
     * @return the right description
     */
    public String getDescription(  )
    {
        String strReturn = I18nService.getLocalizedString( _strDescriptionKey, _locale );
        if ( !strReturn.equals( "" ) )
        {
            return I18nService.getLocalizedString( _strDescriptionKey, _locale );
        }
        else return _strDescriptionKey;
    }

    /**
     * Sets the description of the right to the specified string.
     *
     * @param strDescriptionKey the new description
     */
    public void setDescriptionKey( String strDescriptionKey )
    {
        _strDescriptionKey = ( strDescriptionKey == null ) ? EMPTY_STRING : strDescriptionKey;
    }

    /**
     * Returns the isUpdatable tag of this right ( 1 if the right is updatable, 0 if not ).
     *
     * @return the is_upda
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Sets the name of the right to the specified string.
     *
     * @param strPluginName the new name
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = ( strPluginName == null ) ? EMPTY_STRING : strPluginName;
    }

    /**
     * Returns the feature group of this right.
     *
     * @return the right feature group
         * @since 1.1.1
     */
    public String getFeatureGroup(  )
    {
        return _strFeatureGroup;
    }

    /**
     * Sets the feature group of the right to the specified string.
     *
     * @param strFeatureGroup the new feature group
         * @since 1.1.1
     */
    public void setFeatureGroup( String strFeatureGroup )
    {
        _strFeatureGroup = strFeatureGroup;
    }

    /**
     * Returns the url of the icon associated to the right.
     *
     * @return the icon url
     */
    public String getIconUrl(  )
    {
        return _strIconUrl;
    }

    /**
     * Sets the url of the icon associated to the right.
     *
     * @param strIconUrl the new url
     */
    public void setIconUrl( String strIconUrl )
    {
        _strIconUrl = strIconUrl;
    }

    /**
     * Returns the url of the documentation associated to the right.
     *
     * @return the _strDocumentationUrl
     */
    public String getDocumentationUrl(  )
    {
        return _strDocumentationUrl;
    }

    /**
     * Sets the url of the documentation associated to the right.
     *
     * @param strDocumentationUrl the _strDocumentationUrl to set
     */
    public void setDocumentationUrl( String strDocumentationUrl )
    {
        _strDocumentationUrl = strDocumentationUrl;
    }

    /**
     * Get the right order
     * @return the _order
     */
    public int getOrder(  )
    {
        return _nIdOrder;
    }

    /**
     * Set the right order in feature group
     * @param nOrder the _order to set
     */
    public void setOrder( int nOrder )
    {
        this._nIdOrder = nOrder;
    }
    
    /**
     * Get the external feature boolean
     * @return the _isExternalFeature
     */
    public boolean isExternalFeature(  )
    {
        return _bIsExternalFeature;
    }

    /**
     * Set the external feature boolean
     * @param bExternalFeature the _isExternalFeature to set
     */
    public void setExternalFeature( boolean bExternalFeature )
    {
        this._bIsExternalFeature = bExternalFeature;
    }

    /**
     * Compare the right with the specified right
     * @param o The right to be compared with the instanced right
     * @return The result of comparison
     */
    @Override
    public int compareTo( Right o )
    {
        if ( this.getOrder(  ) > o.getOrder(  ) )
        {
            return 1;
        }
        else if ( this.getOrder(  ) < o.getOrder(  ) )
        {
            return -1;
        }
        else
        {
            return this.getId(  ).compareTo( o.getId(  ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        if ( !( o instanceof Right ) )
        {
            return false;
        }

        return compareTo( (Right) o ) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(  )
    {
        int nIdHash = ( getId(  ) == null ) ? 0 : getId(  ).hashCode(  );

        return ( getOrder(  ) * 100 ) + nIdHash;
    }
}
