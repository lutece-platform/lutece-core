package fr.paris.lutece.portal.web.user;

import java.util.Map;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

public class UsersAdminDashboardComponent extends AdminDashboardComponent
{
	private static final String TEMPLATE_ADMIN_DASHBOARD = "admin/user/user_admindashboard.html";
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getDashboardData( AdminUser user )
	{
		Map<String, Object> model = AdminUserService.getManageAdvancedParameters( user );
		HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_DASHBOARD, user.getLocale(  ), model );
		
		return template.getHtml(  );
	}

}
