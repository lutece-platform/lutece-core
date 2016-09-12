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
import fr.paris.lutece.util.ReferenceItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;


/**
 * This class represents business objects feature group
 */
public class FeatureGroup
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String EMPTY_STRING = "";

    /////////////////////////////////////////////////////////////////////////////////
    // Variables
    private String _strId;
    private String _strDescriptionKey;
    private String _strLabelKey;
    private int _nOrder;
    private Collection<Right> _aFeaturesList = new ArrayList<Right>(  );
    private Locale _locale;

    /**
     * Sets the locale to use
     *
     * @param locale the locale to use
     */
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * Returns the identifier of this feature group
     *
     * @return the identifier of this feature group
     */
    public String getId(  )
    {
        return _strId;
    }

    /**
     * Sets the identifier of the feature group to the specified string.
     *
     * @param strId the new identifier
     */
    public void setId( String strId )
    {
        _strId = strId;
    }

    /**
     * Returns the label of this feature group.
     *
     * @return the feature group label
     */
    public String getLabelKey(  )
    {
        return _strLabelKey;
    }

    /**
     * Returns the label of this feature group.
     * @return the feature group label
     */
    public String getLabel(  )
    {
        return I18nService.getLocalizedString( _strLabelKey, _locale );
    }

    /**
     * Sets the label of the feature group to the specified string.
     *
     * @param strLabelKey the new label
     */
    public void setLabelKey( String strLabelKey )
    {
        _strLabelKey = ( strLabelKey == null ) ? EMPTY_STRING : strLabelKey;
    }

    /**
     * Returns the order of this feature group.
     *
     * @return the feature group order
     */
    public int getOrder(  )
    {
        return _nOrder;
    }

    /**
     * Sets the order of the feature group to the specified int.
     *
     * @param nOrder the new level
     */
    public void setOrder( int nOrder )
    {
        _nOrder = nOrder;
    }

    /**
     * Returns the description of this feature group.
     *
     * @return the feature group description
     */
    public String getDescriptionKey(  )
    {
        return _strDescriptionKey;
    }

    /**
     * Returns the description of this feature group.
     *
     * @return the feature group description
     */
    public String getDescription(  )
    {
        return I18nService.getLocalizedString( _strDescriptionKey, _locale );
    }

    /**
     * Sets the description of the feature group to the specified string.
     *
     * @param strDescriptionKey the new description
     */
    public void setDescriptionKey( String strDescriptionKey )
    {
        _strDescriptionKey = ( strDescriptionKey == null ) ? EMPTY_STRING : strDescriptionKey;
    }

    /**
     * Add a feature to the group
     * @param right The feature to add
     */
    public void addFeature( Right right )
    {
        _aFeaturesList.add( right );
    }

    /**
     * Tells whether or not the feature list is empty
     * @return _aFeaturesList
     */
    public boolean isEmpty(  )
    {
        return _aFeaturesList.isEmpty(  );
    }

    /**
     * Returns all features affected to the group
     * @return a collection
     */
    public Collection<Right> getFeatures(  )
    {
        return _aFeaturesList;
    }
    
    /**
     * Returns a reference item for the feature group
     * 
     * @return a reference item
     */
    public ReferenceItem getReferenceItem(  )
    {
        ReferenceItem item = new ReferenceItem(  );
        item.setCode( _strId );
        item.setName( getLabel(  ) );
        item.setChecked( true );
        return item;
    }
    
    
}
