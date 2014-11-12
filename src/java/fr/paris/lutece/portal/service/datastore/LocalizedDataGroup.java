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
package fr.paris.lutece.portal.service.datastore;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * This is the business class for the object SiteProperty
 */
public class LocalizedDataGroup
{
    private static final String SUFFIX_HELP = ".help";

    // Variables declarations 
    private String _strName;
    private String _strDescription;
    private List<LocalizedData> _listLocalizedData = new ArrayList<LocalizedData>(  );

    /**
     * Constructor
     * @param group The group
     * @param locale The locale
     */
    public LocalizedDataGroup( ILocalizedDataGroup group, Locale locale )
    {
        _strName = I18nService.getLocalizedString( group.getNameKey(  ), locale );
        _strDescription = I18nService.getLocalizedString( group.getDescriptionKey(  ), locale );

        ReferenceList listProperties = DatastoreService.getDataByPrefix( group.getDatastoreKeysPrefix(  ) );

        for ( int i = 0; i < listProperties.size(  ); i++ )
        {
            ReferenceItem item = listProperties.get( i );
            LocalizedData property = new LocalizedData(  );
            property.setKey( item.getCode(  ) );
            property.setValue( item.getName(  ) );
            property.setLabel( I18nService.getLocalizedString( item.getCode(  ), locale ) );
            property.setHelp( I18nService.getLocalizedString( item.getCode(  ) + SUFFIX_HELP, locale ) );
            _listLocalizedData.add( property );
        }
    }

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the localized data list
     *
     * @return The localized data list
     */
    public List<LocalizedData> getLocalizedDataList(  )
    {
        return _listLocalizedData;
    }

    /**
     * Sets the localized data list
     *
     * @param listLocalizedData The localized data list
     */
    public void setLocalizedDataList( List<LocalizedData> listLocalizedData )
    {
        _listLocalizedData = listLocalizedData;
    }
}
