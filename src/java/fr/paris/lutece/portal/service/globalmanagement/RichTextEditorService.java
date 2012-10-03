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
		model.put( MARK_DEFAULT_TEXT_EDITOR, getBackOfficeDefaultEditor( ) );
	}

	public static void addFrontOfficeDefaultEditorToModel( Map<String, Object> model )
	{
		model.put( MARK_DEFAULT_TEXT_EDITOR, getFrontOfficeDefaultEditor( ) );
	}

	public static String getBackOfficeDefaultEditor( )
	{
		String strDefaultEditorName = AppPropertiesService.getProperty( PROPERTY_DEFAULT_EDITOR_BACK_OFFICE );
		return DatastoreService.getDataValue( PARAMETER_DEFAULT_EDITOR_BACK_OFFICE, strDefaultEditorName );
	}

	public static String getFrontOfficeDefaultEditor( )
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
		Collection<RichTextEditor> listRichTextEditor = RichTextEditorHome.findListEditorsForBackOffice( );
		ReferenceList refList = new ReferenceList( );
		for ( RichTextEditor editor : listRichTextEditor )
		{
			ReferenceItem refItem = new ReferenceItem( );
			refItem.setCode( editor.getEditorName( ) );
			refItem.setName( I18nService.getLocalizedString( editor.getDescription( ), locale ) );
			refList.add( refItem );
		}
		return refList;
	}

	public static ReferenceList getListEditorsForFrontOffice( Locale locale )
	{
		Collection<RichTextEditor> listRichTextEditor = RichTextEditorHome.findListEditorsForFrontOffice( );
		ReferenceList refList = new ReferenceList( );
		for ( RichTextEditor editor : listRichTextEditor )
		{
			ReferenceItem refItem = new ReferenceItem( );
			refItem.setCode( editor.getEditorName( ) );
			refItem.setName( I18nService.getLocalizedString( editor.getDescription( ), locale ) );
			refList.add( refItem );
		}
		return refList;
	}
}
