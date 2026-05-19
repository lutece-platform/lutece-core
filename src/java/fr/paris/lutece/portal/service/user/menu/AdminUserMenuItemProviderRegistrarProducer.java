package fr.paris.lutece.portal.service.user.menu;

import java.util.Comparator;

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
	 * Registers every admin user menu item provider on application startup, in
	 * ascending order of {@link AdminUserMenuItemProviderRegistrar#getPriority()}.
	 * Because CDI does not guarantee any iteration order on {@link Instance}, the
	 * priority drives the registration sequence so that {@code insertAfter} /
	 * {@code insertBefore} references always exist when used. External plugins
	 * contributing their own registrar producer just call
	 * {@link AdminUserMenuItemProviderRegistrar#setPriority(int)} to position
	 * their item in the registration stream.
	 *
	 * @param startup
	 *            CDI Startup event
	 */
	protected void adminUserMenuItemProviderRegistrar( @Observes Startup startup )
	{
		_userMenuItemProvider.handlesStream( )
			.map( handle -> {
				AdminUserMenuItemProviderRegistrar registrar = handle.get( );
				registrar.setBeanName( handle.getBean( ).getName( ) );
				registrar.setService( _service );
				return registrar;
			} )
			.sorted( Comparator.comparingInt( AdminUserMenuItemProviderRegistrar::getPriority ) )
			.forEach( AdminUserMenuItemProviderRegistrar::registerAdminUserMenuItemProvider );
	}
    
	@Produces
	@ApplicationScoped
	@Named("modifyPasswordUserMenuItemProvider")
	public AdminUserMenuItemProviderRegistrar produceModifyPassword( ) {
	   	AdminUserMenuItemProviderRegistrar adminMenu= new AdminUserMenuItemProviderRegistrar( );
	   	adminMenu.setProvider(CDI.current().select(ModifyPasswordAdminUserMenuItemProvider.class).get());
		adminMenu.setInsertAfter(_config.getOptionalValue("modifyPasswordUserMenuItemProvider.insertAfter", String.class).orElse(null));
 	    adminMenu.setInsertBefore(_config.getOptionalValue("modifyPasswordUserMenuItemProvider.insertBefore", String.class).orElse( null ));
 	    adminMenu.setPriority( 100 );
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
 	    adminMenu.setPriority( 200 );
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
 	    adminMenu.setPriority( 300 );
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
	    adminMenu.setPriority( 400 );
	    return adminMenu;
    }
}
