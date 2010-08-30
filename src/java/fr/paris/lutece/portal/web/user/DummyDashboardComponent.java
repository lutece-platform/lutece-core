package fr.paris.lutece.portal.web.user;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;

public class DummyDashboardComponent extends AdminDashboardComponent 
{
	@Override
	public String getDashboardData(AdminUser user) {
		return "<div class=\"content-box\"><h2>Dummy dashbaord</h2><p>DummyDashboard</p></div>";
	}

}
