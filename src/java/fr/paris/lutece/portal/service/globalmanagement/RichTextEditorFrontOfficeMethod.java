package fr.paris.lutece.portal.service.globalmanagement;

import freemarker.template.TemplateMethodModel;

import java.util.List;


/**
 * 
 * RichTextEditorFrontOfficeMethod
 * 
 */
public class RichTextEditorFrontOfficeMethod implements TemplateMethodModel
{
	/**
	 * Get the name of the default editor for front office. This class should only be used inside a freemarker template.
	 * @param arg0 Unused
	 * @return The name of the default editor for front office.
	 */
	@Override
	public Object exec( List arg0 )
	{
		return RichTextEditorService.getFrontOfficeDefaultEditor( );
	}
}
