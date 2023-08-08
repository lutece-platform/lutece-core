package fr.paris.lutece.portal.service.user.menu;

import org.eclipse.microprofile.config.Config;

import fr.paris.lutece.portal.business.user.menu.AccessibilityModeAdminUserMenuItemProvider;
import fr.paris.lutece.portal.business.user.menu.DividerAdminUserMenuItemProvider;
import fr.paris.lutece.portal.business.user.menu.LanguageAdminUserMenuItemProvider;
import fr.paris.lutece.portal.business.user.menu.ModifyPasswordAdminUserMenuItemProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
public class AdminUserMenuItemProviderRegistrarProducer {

	@Inject
    private Instance<AdminUserMenuItemProviderRegistrar> _userMenuItemProvider;
	@Inject 
	private AdminUserMenuService _service;
	@Inject
	private Config _config ;
	
	/**
	  * The parameter `@Observes Startup` makes sure that the dependency injection framework
	  * calls this method on system startup. And to do that, it needs to
	  * call registerAdminUserMenuItemProvider()`.
	*/
	protected void adminUserMenuItemProviderRegistrar(@Observes Startup startup) {		
		_userMenuItemProvider.handles().forEach(bean -> {
			AdminUserMenuItemProviderRegistrar userMenu=bean.get();
			userMenu.setBeanName(bean.getBean().getName());
			userMenu.setService(_service);
		}
		);
		_userMenuItemProvider.forEach(action -> action.registerAdminUserMenuItemProvider());
	}
    
	@Produces
	@ApplicationScoped
	@Named("modifyPasswordUserMenuItemProvider")
	public AdminUserMenuItemProviderRegistrar produceModifyPassword( ) {
	   	AdminUserMenuItemProviderRegistrar adminMenu= new AdminUserMenuItemProviderRegistrar( );
	   	adminMenu.setProvider(CDI.current().select(ModifyPasswordAdminUserMenuItemProvider.class).get());
		adminMenu.setInsertAfter(_config.getOptionalValue("modifyPasswordUserMenuItemProvider.insertAfter", String.class).orElse(null));
 	    adminMenu.setInsertBefore(_config.getOptionalValue("modifyPasswordUserMenuItemProvider.insertBefore", String.class).orElse( "accessibilityModeUserMenuItemProvider" ));
 	    
	   	return adminMenu ;
	}
	@Produces
	@ApplicationScoped
	@Named("accessibilityModeUserMenuItemProvider")
	public AdminUserMenuItemProviderRegistrar produceAccessibilityMode( ) {
	   	AdminUserMenuItemProviderRegistrar adminMenu= new AdminUserMenuItemProviderRegistrar( );
	   	adminMenu.setProvider(CDI.current().select(AccessibilityModeAdminUserMenuItemProvider.class).get());
	   	adminMenu.setInsertAfter(_config.getOptionalValue("accessibilityModeUserMenuItemProvider.insertAfter", String.class).orElse("modifyPasswordUserMenuItemProvider"));
 	    adminMenu.setInsertBefore(_config.getOptionalValue("accessibilityModeUserMenuItemProvider.insertBefore", String.class).orElse( null ));

	   	return adminMenu ;
	}
    @Produces
    @ApplicationScoped
    @Named("languageUserMenuItemProvider")
    public AdminUserMenuItemProviderRegistrar produceLanguage( ) {
    	AdminUserMenuItemProviderRegistrar adminMenu= new AdminUserMenuItemProviderRegistrar( );
    	adminMenu.setProvider(CDI.current().select(LanguageAdminUserMenuItemProvider.class).get());
    	adminMenu.setInsertAfter(_config.getOptionalValue("languageUserMenuItemProvider.insertAfter", String.class).orElse("accessibilityModeUserMenuItemProvider"));
 	    adminMenu.setInsertBefore(_config.getOptionalValue("languageUserMenuItemProvider.insertBefore", String.class).orElse( null ));

    	return adminMenu ;
    }
    @Produces
	@ApplicationScoped
	@Named("dividerUserMenuItemProvider")
	public AdminUserMenuItemProviderRegistrar produceDivider1User( ) {
	   	AdminUserMenuItemProviderRegistrar adminMenu= new AdminUserMenuItemProviderRegistrar( );
	   	adminMenu.setProvider(CDI.current().select(DividerAdminUserMenuItemProvider.class).get());
	    adminMenu.setInsertAfter(_config.getOptionalValue("dividerUserMenuItemProvider.insertAfter", String.class).orElse("accessibilityModeUserMenuItemProvider"));
	    adminMenu.setInsertBefore(_config.getOptionalValue("dividerUserMenuItemProvider.insertBefore", String.class).orElse( null ));

	    return adminMenu;
    }
}
