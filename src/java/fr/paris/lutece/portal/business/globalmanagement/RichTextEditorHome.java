package fr.paris.lutece.portal.business.globalmanagement;

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;



public class RichTextEditorHome
{
	private static IRichTextEditorDAO _dao = SpringContextService.getBean( IRichTextEditorDAO.BEAN_NAME );

	/**
	 * Get collection of RichTextEditor for back office
	 * @return The collection of RichTextEditor for back office
	 */
	public static Collection<RichTextEditor> findListEditorsForBackOffice( )
	{
		return _dao.findEditors( Boolean.TRUE );
	}

	/**
	 * Get the collection of RichTextEditor for front office
	 * @return The collection of RichTextEditor for front office
	 */
	public static Collection<RichTextEditor> findListEditorsForFrontOffice( )
	{
		return _dao.findEditors( Boolean.FALSE );
	}
}
