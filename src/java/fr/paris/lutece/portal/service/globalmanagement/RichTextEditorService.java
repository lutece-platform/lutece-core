/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.portal.service.globalmanagement;

import fr.paris.lutece.portal.business.globalmanagement.RichTextEditor;
import fr.paris.lutece.portal.business.globalmanagement.RichTextEditorHome;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;


public class RichTextEditorService
{
    private static final String PARAMETER_DEFAULT_EDITOR_BACK_OFFICE = "core.backOffice.defaultEditor";
    private static final String PARAMETER_DEFAULT_EDITOR_FRONT_OFFICE = "core.frontOffice.defaultEditor";
    private static final String PROPERTY_DEFAULT_EDITOR_BACK_OFFICE = "lutece.backOffice.defaultEditor";
    private static final String PROPERTY_DEFAULT_EDITOR_FRONT_OFFICE = "lutece.frontOffice.defaultEditor";
    public static final String MARK_DEFAULT_TEXT_EDITOR = "default_text_editor";

    public static void addBackOfficeDefaultEditortToModel( Map<String, Object> model )
    {
        model.put( MARK_DEFAULT_TEXT_EDITOR, getBackOfficeDefaultEditor(  ) );
    }

    public static void addFrontOfficeDefaultEditorToModel( Map<String, Object> model )
    {
        model.put( MARK_DEFAULT_TEXT_EDITOR, getFrontOfficeDefaultEditor(  ) );
    }

    public static String getBackOfficeDefaultEditor(  )
    {
        String strDefaultEditorName = AppPropertiesService.getProperty( PROPERTY_DEFAULT_EDITOR_BACK_OFFICE );

        return DatastoreService.getDataValue( PARAMETER_DEFAULT_EDITOR_BACK_OFFICE, strDefaultEditorName );
    }

    public static String getFrontOfficeDefaultEditor(  )
    {
        String strDefaultEditorName = AppPropertiesService.getProperty( PROPERTY_DEFAULT_EDITOR_FRONT_OFFICE );

        return DatastoreService.getDataValue( PARAMETER_DEFAULT_EDITOR_FRONT_OFFICE, strDefaultEditorName );
    }

    public static void updateBackOfficeDefaultEditor( String strEditorUrl )
    {
        DatastoreService.setDataValue( PARAMETER_DEFAULT_EDITOR_BACK_OFFICE, strEditorUrl );
    }

    public static void updateFrontOfficeDefaultEditor( String strEditorUrl )
    {
        DatastoreService.setDataValue( PARAMETER_DEFAULT_EDITOR_FRONT_OFFICE, strEditorUrl );
    }

    public static ReferenceList getListEditorsForBackOffice( Locale locale )
    {
        Collection<RichTextEditor> listRichTextEditor = RichTextEditorHome.findListEditorsForBackOffice(  );
        ReferenceList refList = new ReferenceList(  );

        for ( RichTextEditor editor : listRichTextEditor )
        {
            ReferenceItem refItem = new ReferenceItem(  );
            refItem.setCode( editor.getEditorName(  ) );
            refItem.setName( I18nService.getLocalizedString( editor.getDescription(  ), locale ) );
            refList.add( refItem );
        }

        return refList;
    }

    public static ReferenceList getListEditorsForFrontOffice( Locale locale )
    {
        Collection<RichTextEditor> listRichTextEditor = RichTextEditorHome.findListEditorsForFrontOffice(  );
        ReferenceList refList = new ReferenceList(  );

        for ( RichTextEditor editor : listRichTextEditor )
        {
            ReferenceItem refItem = new ReferenceItem(  );
            refItem.setCode( editor.getEditorName(  ) );
            refItem.setName( I18nService.getLocalizedString( editor.getDescription(  ), locale ) );
            refList.add( refItem );
        }

        return refList;
    }
}
