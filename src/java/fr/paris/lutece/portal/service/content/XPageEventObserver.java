/*
 * Copyright (c) 2002-2025, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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
import jakarta.inject.Named;

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
	            Set<XPageApplicationEntry> entries = getXPageApplicationEntries( plugin );
	    		if (entries.isEmpty()) {
	                AppLogService.debug("No XPage applications found for plugin: {}", plugin.getName());
	                return;
	            }
		    	for ( XPageApplicationEntry entry : entries )
				{
				 // Register the XPage application
		    	    String xPagePropertieEnabled = entry.getPluginName( ) + ".xpage." + entry.getId( )+".enabled";
	                entry.setEnabled(AppPropertiesService.getPropertyBoolean(xPagePropertieEnabled, true));
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
     * @param plugin The plugin
     * @return Set of discovered XPage application entries
     */
     private Set<XPageApplicationEntry> getXPageApplicationEntries( Plugin plugin ) {
    	 
    	 Set<XPageApplicationEntry> entries = new HashSet<>();
    	 String beanNamePrefix = plugin.getName() + ".xpage.";
         CDI<Object> cdi = CDI.current();
         Set<Bean<?>> beans = cdi.getBeanManager().getBeans(MVCApplication.class);
         for (Bean<?> bean : beans) {
            // check if the bean has a @Controller annotation
            Controller controllerAnnotation = bean.getBeanClass().getAnnotation(Controller.class);
            if (controllerAnnotation != null) {
            	Named namedAnnotation = bean.getBeanClass().getAnnotation(Named.class);
                String xpageName = controllerAnnotation.xpageName();
                if( XPageAppService.getApplicationEntry(xpageName) == null 
                		&& namedAnnotation != null && namedAnnotation.value().startsWith(beanNamePrefix) ) {
   	                XPageApplicationEntry entry = new XPageApplicationEntry();
   	                entry.setId(xpageName);
   	                entry.setPluginName(plugin.getName());
   	                entries.add(entry);
                }else {
                    AppLogService.debug("XPage '{}' is already registered, skipping", xpageName);
                }
            }
         }
         return entries;
    }
}
