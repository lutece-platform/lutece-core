package fr.paris.lutece.portal.service.globalmanagement;

import freemarker.template.TemplateMethodModel;

import java.util.List;


/**
 * 
 * RichTextEditorBackOfficeMethod
 * 
 */
public class RichTextEditorBackOfficeMethod implements TemplateMethodModel
{
	/**
	 * Get the name of the default editor for back office. This class should only be used inside a freemarker template.
	 * @param arg0 Unused
	 * @return The name of the default editor for back office.
	 */
	@Override
	public Object exec( List arg0 )
	{
		return RichTextEditorService.getBackOfficeDefaultEditor( );
	}
}
