<@row>
	<@columns>
		<@box>
			<@boxHeader title='Liste des plugins' boxTools=true>
				<@tform method='post' type='inline' align='left'>
					<@formGroup labelFor='plugin_type' labelKey='#i18n{portal.system.manage_plugins.buttonFilter}' hideLabel=['all'] formStyle='inline'>
						<@inputGroup>
							<@input type='text' name='search_plugins' size='sm' id='search_plugins' placeHolder='#i18n{portal.system.manage_plugins.buttonFilter}' params='autocomplete="off"' />
							<@inputGroupItem>
								<@button type='submit' size='sm' title='#i18n{portal.system.manage_plugins.buttonFilter}' buttonIcon='filter' hideTitle=['all'] />
							</@inputGroupItem>
						</@inputGroup>
					</@formGroup>
        		</@tform>
				<@tform method='post' name='FilterPluginsForm' action='jsp/admin/system/ManagePlugins.jsp' type='inline' align='right' hide=['xs']>
					<@formGroup labelKey='#i18n{portal.system.manage_plugins.labelComboFilter}' labelFor='plugin_type' hideLabel=['all'] formStyle='inline'>
						<@inputGroup>
							<@select sort=true name='plugin_type' default_value='${current_filter}' items=filter_list size='sm' />
							<@inputGroupItem>
							<@button type='submit' size='sm' buttonIcon='search' title='Search' hideTitle=['all'] />
							</@inputGroupItem>
						</@inputGroup>
					</@formGroup>
				</@tform>
    			<!--<@button type='button' size='sm' buttonIcon='list' title='Layout' hideTitle=['all'] id='th'/>-->
			</@boxHeader>
      <@boxBody>
       <#if plugins_list?has_content >
				<@row id='plugins'>
					<@columns sm=12 md=6 lg=4 class='plugins'>
						<@box id='' params='data-plugin="${core.name}"' class='plugin-box plugin'>
							<#assign logo>
								<@img url=core.iconUrl alt='logo' title=core.name />
							</#assign>
							<@boxHeader title='${logo} ${core.name} #i18n{portal.system.manage_plugins.titleCorePlugin} ${core.version}' boxTools=true>
							
							</@boxHeader>
							<@boxBody>
								<p>#i18n{portal.system.manage_plugins.labelVersion}: ${core.version}</p>
								<p>#i18n{${core.description}}</p>
								<p><@icon style='user' /> <em>#i18n{portal.system.manage_plugins.labelAuthor} : #i18n{${core.provider}}</em></p>	
								<p>	
									<@icon style='globe' /> <em>#i18n{portal.system.manage_plugins.labelAuthorUrl} : <@link href='${core.providerUrl}' target='_blank'>${core.providerUrl}</@link></em>
								</p> 
							</@boxBody>
							<@boxFooter>
								<@tform method='post' type='inline' align='right' action='jsp/admin/system/ViewPluginDescription.jsp?plugin_name=${core.name}'>
									<@button type='submit' title='#i18n{portal.system.manage_plugins.actionDisplay}' buttonIcon='info-circle' hideTitle=['all'] size='sm' />
								</@tform>
							</@boxFooter>
						</@box>
					</@columns>
				
					<#list plugins_list as plugin >
						<@columns sm=12 md=6 lg=4 class='plugins'>
						<@box id='plugin_${plugin.name}_${plugin.version}' params='data-plugin="${plugin.name}"' class='plugin-box plugin'>
							<#assign logo>
								<@img url='${plugin.iconUrl}' alt='logo' title='${plugin.name}' />
							</#assign>
							<@boxHeader title='${logo} ${plugin.name}' boxTools=true>
								<#if plugin.installed>
									<@tag color='success' title='#i18n{portal.system.manage_caches.imgAltEnable}'>
										<@icon style='check-circle' />
										#i18n{portal.system.manage_caches.imgAltEnable}
									</@tag>
                				<#else>
									<@tag color='danger' title='#i18n{portal.system.manage_caches.imgAltDisable}'>
										<@icon style='times-circle' />
										#i18n{portal.system.manage_caches.imgAltDisable}
									</@tag>
								</#if>
							</@boxHeader>
              				<@boxBody>
												<p>#i18n{portal.system.manage_plugins.labelVersion}: ${plugin.version}</p>
												<p>#i18n{${plugin.description}}</p>
												<p><@icon style='user' /> <em>#i18n{portal.system.manage_plugins.labelAuthor} : #i18n{${plugin.provider}}</em></p>	
												<p>	
													<@icon style='globe' /> <em>#i18n{portal.system.manage_plugins.labelAuthorUrl} : <@link href='${plugin.providerUrl}' target='_blank'>${plugin.providerUrl}</@link></em>
												</p> 
              				</@boxBody>
							<@boxFooter>
								<@btnToolbar>
								<#if plugin.installed>
									<#if plugin.dbPoolRequired>
										<@btnGroup>
										<@tform method='post' type='inline' align='right' action='jsp/admin/system/DoModifyPluginPool.jsp#plugin_${plugin.name}_${plugin.version}'>
										<@inputGroup>
											<@select name='db_pool_name' default_value='${plugin.dbPoolName}' items=pools_list sort=true size='sm' />
											<@input type='hidden' name='plugin_name' value='${plugin.name}' />
											<@input type='hidden' name='token' value='${token}' />
											<@inputGroupItem>
												<@button type='submit' title='#i18n{portal.system.manage_plugins.buttonModifyDbPool}' buttonIcon='check' size='sm' hideTitle=['all'] />
											</@inputGroupItem>
										</@inputGroup>
										</@tform>
										</@btnGroup>
									</#if>   
								</#if>
								<@btnGroup>
								<@tform method='post' type='inline' align='right' action='jsp/admin/system/ViewPluginDescription.jsp?plugin_name=${plugin.name}'>
									<@button type='submit' title='' buttonIcon='info-circle' size='sm' hideTitle=['all'] />
								</@tform>
									<#if plugin.installed >
									<@tform method='post' type='inline' align='right' action='jsp/admin/system/ConfirmUninstallPlugin.jsp'>
										<@input type='hidden' name='plugin_name' value='${plugin.name}' />
										<@button type='submit' title='#i18n{portal.system.manage_plugins.buttonDisable}' buttonIcon='stop' size='sm' hideTitle=['all'] color='danger' />
									</@tform>
									<#else>
									<@tform method='post' type='inline' align='right' action='jsp/admin/system/DoInstallPlugin.jsp#plugin_${plugin.name}_${plugin.version}'>
										<@input type='hidden' name='plugin_name' value='${plugin.name}' />
										<@input type='hidden' name='token' value='${token}' />
										<@button type='submit' title='#i18n{portal.system.manage_plugins.buttonEnable}' buttonIcon='play' size='sm' hideTitle=['all'] color='success' />
                  					</@tform>
									</#if>
								</@btnGroup>
								</@btnToolbar>
							</@boxFooter>
						</@box>
					</@columns>
        		</#list>	
     			</@row>
			<#else>
				<@alert color='danger'>#i18n{portal.system.manage_plugins.filterNoResult}</@alert>
			</#if>
      </@boxBody>
    </@box>
  </@columns>
</@row>

<script>
    /*function switchList(){
        var addOrRemove;
        $(".plugins").toggleClass("col-xs-12 col-sm-12 col-md-12 col-lg-12", addOrRemove).toggleClass("col-xs-12 col-sm-6 col-md-4 col-lg-4", addOrRemove);
        $(".plugin-box").toggleClass("pull-left", addOrRemove);
        $(".plugin-box").toggleClass("plugin", addOrRemove);
        $(".plugin-box > div").toggleClass("pull-left", addOrRemove).toggleClass("col-xs-12 col-sm-4 col-md-4 col-lg-4", addOrRemove);
    }*/
    
    /* Get list or box plugin's presentation */
    /*$("#th").click( function(){
        switchList();
    });*/
    
    /* Filter plugins list */
    $("#search_plugins").on("keyup", function () {
        var addOrRemove;
        var search = $("#search_plugins").val();
        $(".plugin-box").each(function (index) {
            var pluginName = $(this).attr("data-plugin");
            if ( pluginName.match(search) == null ){
                $(this).parent().slideUp(200).fadeOut(500);
            } else {
                $(this).parent().slideDown(200).fadeIn(500);
            }
        });
    });
    
    $( function(){
         switchList();
    });
</script>