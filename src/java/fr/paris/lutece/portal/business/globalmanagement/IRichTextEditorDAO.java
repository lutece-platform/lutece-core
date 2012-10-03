package fr.paris.lutece.portal.business.globalmanagement;

import java.util.Collection;


public interface IRichTextEditorDAO
{
	public static final String BEAN_NAME = "richTextEditorDAO";

	/**
	 * Get the collection of RichTextEditor for back or front office
	 * @param bBackOffice True if the list should contain back office editors, false if it should contain front office editors.
	 * @return The collection of RichTextEditor for back or front office
	 */
	Collection<RichTextEditor> findEditors( Boolean bBackOffice );
}
