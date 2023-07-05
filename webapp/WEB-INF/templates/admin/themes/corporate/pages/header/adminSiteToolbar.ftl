<#-- Macro: adminSiteToolbar
Description: Generates a toolbar for use in the site admin panel. It generates a Bootstrap toolbar that includes buttons for various site management tasks, such as searching for a page, changing the display size, deleting a page, adding a child page, and viewing the site map.
-->
<#macro adminSiteToolbar >
<@btnToolbar>
	<@btnGroup>
		<@tform type='inline' action='jsp/admin/site/AdminSite.jsp' role='search'>
			<@input type='number' name='page_id' id='page_id' title='${i18n("portal.site.admin_page.buttonSearchPage")}' placeHolder='22' params=' style="width:4rem;"' />
			<@button type='submit' color='primary' title='${i18n("portal.site.admin_page.buttonSearchPage")}' hideTitle=['all'] buttonIcon='search' />
		</@tform>
	</@btnGroup>
	<@btnGroup class='ms-1'>
		<@aButton href='' id='display-desktop' title='#i18n{portal.site.admin_page.buttonLargeScreen}' buttonIcon='desktop' color='primary' class='btn-preview' hideTitle=['all'] />
		<@aButton href='' id='display-tablet' title='#i18n{portal.site.admin_page.buttonTablet}' buttonIcon='tablet' color='primary' class='btn-preview' hideTitle=['all'] />
		<@aButton href='' id='display-phone' title='#i18n{portal.site.admin_page.buttonSmartphone}' buttonIcon='mobile' color='primary' class='btn-preview' hideTitle=['all'] />
	</@btnGroup>
	<#if page.id != 1>
		<@btnGroup class='ms-1'>
			<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.parentPageId}' color='success' buttonIcon='arrow-up' title='#i18n{portal.site.admin_page.buttonUpToParentPage}' hideTitle=['all'] />
			<@aButton href='jsp/admin/site/RemovePage.jsp?page_id=${page.id}' color='danger btn-icon' buttonIcon='trash text-white' title='#i18n{portal.site.admin_page.buttonDeletePage}' hideTitle=['all']  />
		</@btnGroup>
	</#if>
	<@btnGroup class='ms-1'>
	<#if portlet_types_list?has_content>
		<@aButton class='dropdown-toggle' size='sm' id='portlet-type' dropdownMenu=true href='#' title='#i18n{portal.site.admin_page.labelPortletPage}' buttonIcon='th-large' hideTitle=['xs','sm','md']>
			<#list portlet_types_list?sort_by("name") as portlet_type>
				<@dropdownItem class='portlet-type-ref' href='jsp/admin/DoCreatePortlet.jsp?portlet_type_id=${portlet_type.id}&amp;page_id=${page.id}' target='preview' title='${portlet_type.name}'>
					${portlet_type.name}
				</@dropdownItem>
			</#list>
		</@aButton>
	</#if>
	<#if extendableResourceActionsHtml?? && extendableResourceActionsHtml?has_content>
		${extendableResourceActionsHtml!}
	</#if>
	</@btnGroup>
	<@btnGroup class='ms-1'>
		<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.id}&amp;param_block=2' buttonIcon='wrench' title='#i18n{portal.site.admin_page.labelPageProperty}' hideTitle=['xs','sm','md']  />
	</@btnGroup>
	<@btnGroup class='ms-1'>
		<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.id}&amp;param_block=5'  buttonIcon='plus' title='#i18n{portal.site.admin_page.labelChildPagePage}' hideTitle=['xs','sm','md'] />
	</@btnGroup>
	<@btnGroup class='ms-1'>
		<@aButton href='jsp/admin/site/AdminMap.jsp?page_id=${page.id }' buttonIcon='sitemap' title='Page ${page.name} - Id ${page.id}' hideTitle=['all'] ><@span hide=['all']>#i18n{portal.site.admin_page.tabAdminMapSite}</@span></@aButton>
	</@btnGroup>
	<@btnGroup class='ms-1'>
		<@aButton href='jsp/site/Portal.jsp?page_id=${page.id}' title='#i18n{portal.site.admin_page.labelShowPage}' buttonIcon='external-link' color='info' class='btn-preview' target='_blank' hideTitle=['all'] />
	</@btnGroup>
	<#--  
	<@btnGroup class='ms-1'>
		<@aButton href='' id='fullscreen' title='#i18n{portal.site.admin_page.buttonFullscreen}' buttonIcon='arrows-alt' color='secondary'  hideTitle=['all'] />
	</@btnGroup>  
	-->
</@btnToolbar>
</#macro>