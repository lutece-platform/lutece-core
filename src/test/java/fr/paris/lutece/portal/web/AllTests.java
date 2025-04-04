package fr.paris.lutece.portal.web;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import fr.paris.lutece.portal.web.admin.AdminMapJspBeanTest;
import fr.paris.lutece.portal.web.admin.AdminMenuJspBeanTest;
import fr.paris.lutece.portal.web.admin.AdminMessageJspBeanTest;
import fr.paris.lutece.portal.web.features.FeaturesGroupJspBeanTest;
import fr.paris.lutece.portal.web.features.LevelsJspBeanTest;
import fr.paris.lutece.portal.web.insert.InsertServiceJspBeanTest;
import fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBeanTest;
import fr.paris.lutece.portal.web.rbac.RoleManagementJspBeanTest;
import fr.paris.lutece.portal.web.search.SearchAppTest;
import fr.paris.lutece.portal.web.search.SearchIndexationJspBeanTest;
import fr.paris.lutece.portal.web.style.PageTemplatesJspBeanTest;
import fr.paris.lutece.portal.web.style.StylesJspBeanTest;
import fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBeanTest;
import fr.paris.lutece.portal.web.system.PluginJspBeanTest;
import fr.paris.lutece.portal.web.system.SystemJspBeanTest;
import fr.paris.lutece.portal.web.xpages.SiteMapAppTest;

@Suite
@SelectClasses({ AdminMapJspBeanTest.class, AdminMenuJspBeanTest.class, AdminMessageJspBeanTest.class, FeaturesGroupJspBeanTest.class, LevelsJspBeanTest.class,
        InsertServiceJspBeanTest.class, InsertServiceSelectorJspBeanTest.class, RoleManagementJspBeanTest.class, SearchAppTest.class,
        SearchIndexationJspBeanTest.class, PageTemplatesJspBeanTest.class, StylesJspBeanTest.class, StyleSheetJspBeanTest.class, PluginJspBeanTest.class,
        SystemJspBeanTest.class, SiteMapAppTest.class, PortalJspBeanTest.class })
public final class AllTests
{

}
