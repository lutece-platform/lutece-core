package fr.paris.lutece.portal.web.globalmanagement;

import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.globalmanagement.RichTextEditorService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class EditorChoiceLutecePanelJspBean extends AbstractGMLutecePanel
{
	private static final String LABEL_TITLE_EDITOR_CHOICE = "portal.globalmanagement.editorChoice.labelEditorChoice";

	private static final String PARAM_EDITOR_BACK_OFFICE = "editor_back_office";
	private static final String PARAM_EDITOR_FRONT_OFFICE = "editor_front_office";

	private static final String MARK_LIST_EDITORS_BACK_OFFICE = "listEditorsBackOffice";
	private static final String MARK_LIST_EDITORS_FRONT_OFFICE = "listEditorsFrontOffice";
	private static final String MARK_CURRENT_EDITOR_BACK_OFFICE = "current_editor_back_office";
	private static final String MARK_CURRENT_EDITOR_FRONT_OFFICE = "current_editor_front_office";

	private static final String TEMPLATE_EDITOR_CHOICE_PANEL = "admin/globalmanagement/panel/editor_choice_panel.html";

	private static final String JSP_URL_GLOBAL_MANAGEMENT = "jsp/admin/globalmanagement/GetGlobalManagement.jsp";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPanelContent( )
	{
		Map<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_LIST_EDITORS_BACK_OFFICE, RichTextEditorService.getListEditorsForBackOffice( AdminUserService.getLocale( getRequest( ) ) ) );
		model.put( MARK_CURRENT_EDITOR_BACK_OFFICE, RichTextEditorService.getBackOfficeDefaultEditor( ) );

		model.put( MARK_LIST_EDITORS_FRONT_OFFICE, RichTextEditorService.getListEditorsForFrontOffice( AdminUserService.getLocale( getRequest( ) ) ) );
		model.put( MARK_CURRENT_EDITOR_FRONT_OFFICE, RichTextEditorService.getFrontOfficeDefaultEditor( ) );

		HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EDITOR_CHOICE_PANEL, getLocale( ), model );
		return template.getHtml( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPanelKey( )
	{
		return LABEL_TITLE_EDITOR_CHOICE;
	}

	/**
	 * Returns the panel's order. This panel is a first panel.
	 * @return 1
	 */
	@Override
	public int getPanelOrder( )
	{
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPanelTitle( )
	{
		return I18nService.getLocalizedString( LABEL_TITLE_EDITOR_CHOICE, AdminUserService.getLocale( getRequest( ) ) );
	}

	public String doUpdateBackOfficeEditor( HttpServletRequest request )
	{
		String strEditorName = request.getParameter( PARAM_EDITOR_BACK_OFFICE );
		RichTextEditorService.updateBackOfficeDefaultEditor( strEditorName );
		return AppPathService.getBaseUrl( request ) + JSP_URL_GLOBAL_MANAGEMENT;
	}

	public String doUpdateFrontOfficeEditor( HttpServletRequest request )
	{
		String strEditorName = request.getParameter( PARAM_EDITOR_FRONT_OFFICE );
		RichTextEditorService.updateFrontOfficeDefaultEditor( strEditorName );
		return AppPathService.getBaseUrl( request ) + JSP_URL_GLOBAL_MANAGEMENT;
	}

}
