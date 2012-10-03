package fr.paris.lutece.portal.business.globalmanagement;

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


public class RichTextEditorDAO implements IRichTextEditorDAO
{
	private static final String SQL_QUERY_FIND_EDITORS_BY_TYPE = " select editor_name, editor_description FROM core_text_editor WHERE backOffice = ? ORDER BY editor_name asc ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<RichTextEditor> findEditors( Boolean bBackOffice )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_EDITORS_BY_TYPE );
		daoUtil.setBoolean( 1, bBackOffice );
		Collection<RichTextEditor> listRes = new ArrayList<RichTextEditor>( );
		daoUtil.executeQuery( );
		while ( daoUtil.next( ) )
		{
			RichTextEditor editor = new RichTextEditor( );
			editor.setEditorName( daoUtil.getString( 1 ) );
			editor.setDescription( daoUtil.getString( 2 ) );
			editor.setBackOffice( bBackOffice );
			listRes.add( editor );
		}
		daoUtil.free( );
		return listRes;
	}
}
