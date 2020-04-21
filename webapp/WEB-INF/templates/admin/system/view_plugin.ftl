<@row>
	<@columns>
   		<@box>
      		<@boxHeader>
				<@link href='jsp/admin/system/ManagePlugins.jsp#${name}' title='${name}'>
					<@img url=iconUrl alt=name title=name /> #i18n{portal.system.view_plugin.boxTitlePluginDetail} "${name}" 
				</@link>
			</@boxHeader>
			<@boxBody>
				<p>#i18n{portal.system.view_plugin.labelVersion} ${version}</p>
		        <p>#i18n{portal.system.view_plugin.labelDescription} #i18n{${description}}</p>
		        <p>#i18n{portal.system.view_plugin.labelAuthor} #i18n{${provider}}</p>
		        <p>#i18n{portal.system.view_plugin.labelProvider} <@link href='${providerUrl}' target='_blank'>${providerUrl}</@link></p>
		        <p>#i18n{portal.system.view_plugin.labelCoreCompatibility} min : ${minCoreVersion?default("")} - max : ${maxCoreVersion?default("")}</p>
	      </@boxBody>
    </@box>
  </@columns>
</@row>

<#if rights?has_content>
<@row>
	<@columns>
		<@box>
			<@boxHeader title='#i18n{portal.system.view_plugin.boxTitleAdministrationFunctionnalities}' />
			<@boxBody>
				<@ul>
					<#list rights as feature>
						<li>
							<#if feature.iconUrl?exists>
								<@img url=feature.iconUrl alt=name title=name />
							</#if>
							<h3>${feature.name}</h3>
							<p>${feature.description}</p>
						</li>
					</#list>
				</@ul>
			</@boxBody>
    </@box>
  </@columns>
</@row>
</#if>

<#if portletTypes?has_content>
<@row>
	<@columns>
    <@box>
      <@boxHeader title='#i18n{portal.system.view_plugin.boxTitlePortletsType}' />
		<@boxBody>
			<@ul>
	  			<#list portletTypes as portlet >
				<li>
					<h3>${portlet.name}</h3>
				</li>		
	  			</#list>
			</@ul>
		</@boxBody>
    </@box>
  </@columns>
</@row>     
</#if> 

<#if applications?has_content>
<@row>
	<@columns>
		<@box>
      <@boxHeader title='#i18n{portal.system.view_plugin.boxTitleXpageApplications}' />
			<@boxBody>
				<@ul>
					<#list applications as application >
						<li>
							<h3>${application.id}<#if !application.enabled> <@tag><@icon style='ban' /> ${i18n("portal.system.view_plugin.disabled")}</@tag></#if></h3>
							<p>${application.className!}</p>
						</li>		
					</#list>
				</@ul>
			</@boxBody>
		</@box>
  </@columns>
</@row>      
</#if> 

<#if contentServices?has_content>
<@row>
	<@columns>
		<@box>
			<@boxHeader title='#i18n{portal.system.view_plugin.boxTitleContentServices}' />
			<@boxBody>
				<@ul>
					<#list contentServices as contentService >
						<li>
							<h3>${contentService.className}</h3>
						</li>		
					</#list>
				</@ul>
			</@boxBody>
		</@box>
	</@columns>
</@row>        
</#if> 

<#if insertServices?has_content>
<@row>
	<@columns>
		<@box>
			<@boxHeader title='#i18n{portal.system.view_plugin.boxTitleInsertServices}' />
			<@boxBody>
				<@ul>
					<#list insertServices as insertService >
					<li>
						<h3>${insertService.name}</h3>
					</li>		
					</#list>
				</@ul>
			</@boxBody>
    </@box>
  </@columns>
</@row>   
</#if>