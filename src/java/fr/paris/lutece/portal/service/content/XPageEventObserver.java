package fr.paris.lutece.portal.service.content;

import java.util.HashSet;
import java.util.Set;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginEvent;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.CDI;

@ApplicationScoped
public  class XPageEventObserver {
	
    private static final String LOG_REGISTERED_XPAGES = "Registered {} XPage applications for plugin: {}";

	/**
     * Observes plugin events and Register an application by its entry defined in @Controller annotation
     * 
     * @param event The plugin event
	 * @throws LuteceInitException 
     */
    public void onPluginEvent(@Observes PluginEvent event) throws LuteceInitException {
        
    	if(event.getEventType()== PluginEvent.PLUGIN_INSTALLED ){
    		try {
	            Plugin plugin = event.getPlugin();
	            Set<XPageApplicationEntry> entries = getXPageApplicationEntries();
	    		if (entries.isEmpty()) {
	                AppLogService.debug("No XPage applications found for plugin: {}", plugin.getName());
	                return;
	            }
		    	for ( XPageApplicationEntry entry : entries )
				{
				 // Register the XPage application
		    	    String xPagePropertieEnabled = entry.getPluginName( ) + ".xpage." + entry.getId( )+".enabled";
	                entry.setEnabled(AppPropertiesService.getPropertyBoolean(xPagePropertieEnabled, true));           
		    		entry.setPluginName( event.getPlugin( ).getName( ) );
		    		XPageAppService.registerXPageApplication( entry );
				}
	            AppLogService.info(LOG_REGISTERED_XPAGES, entries.size(), plugin.getName());
    		} catch (Exception e) {
                AppLogService.error("Failed to process XPage applications for plugin: {}", 
                    event.getPlugin().getName(), e);
                // Don't propagate exception to avoid breaking plugin installation
            }
    	}
  	 }
    
    /**
     * Discovers XPage applications by scanning CDI beans with @Controller annotation
     * 
     * @return Set of discovered XPage application entries
     */
     private Set<XPageApplicationEntry> getXPageApplicationEntries() {
    	 
    	 Set<XPageApplicationEntry> entries = new HashSet<>();
         CDI<Object> cdi = CDI.current();
         Set<Bean<?>> beans = cdi.getBeanManager().getBeans(MVCApplication.class);
         for (Bean<?> bean : beans) {
            // check if the bean has a @Controller annotation
            Controller controllerAnnotation = bean.getBeanClass().getAnnotation(Controller.class);
            if (controllerAnnotation != null) {
                String xpageName = controllerAnnotation.xpageName();
                if( XPageAppService.getApplicationEntry(xpageName) == null ) {
   	                XPageApplicationEntry entry = new XPageApplicationEntry();
   	                entry.setId(xpageName);
   	                entries.add(entry);
                }else {
                    AppLogService.debug("XPage '{}' is already registered, skipping", xpageName);
                }
            }
         }
         return entries;
    }
}
