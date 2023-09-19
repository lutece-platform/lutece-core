<#-- Macro: adminSiteToolbar
Description: Generates a toolbar for use in the site admin panel. It generates a Bootstrap toolbar that includes buttons for various site management tasks, such as searching for a page, changing the display size, deleting a page, adding a child page, and viewing the site map.
-->
<#macro adminSiteToolbar >
<@btnToolbar>
	<@btnGroup>
		<@tform type='inline' action='jsp/admin/site/AdminSite.jsp' role='search'>
			<@input type='number' name='page_id' id='page_id' min=1 title='${i18n("portal.site.admin_page.buttonSearchPage")}' placeHolder='22' params=' style="width:4rem;"' />
			<@button type='submit' color='primary' title='${i18n("portal.site.admin_page.buttonSearchPage")}' hideTitle=['all'] buttonIcon='search' />
		</@tform>
	</@btnGroup>
	<@btnGroup class='ms-1'>
		<@aButton href='' id='display-desktop' title='#i18n{portal.site.admin_page.buttonXLargeScreen}' buttonIcon='device-desktop' color='primary' class='btn-preview active' hideTitle=['all'] />
		<@aButton href='' id='display-laptop' title='#i18n{portal.site.admin_page.buttonLargeScreen}' buttonIcon='device-laptop' color='primary' class='btn-preview' hideTitle=['all'] />
		<@aButton href='' id='display-tablet' title='#i18n{portal.site.admin_page.buttonTablet}' buttonIcon='device-tablet' color='primary' class='btn-preview' hideTitle=['all'] />
		<@aButton href='' id='display-phone' title='#i18n{portal.site.admin_page.buttonSmartphone}' buttonIcon='device-mobile' color='primary' class='btn-preview' hideTitle=['all'] />
	</@btnGroup>
	<#if page.id != 1>
		<@btnGroup class='ms-1'>
			<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.parentPageId}' color='success' buttonIcon='arrow-up' size='' title='#i18n{portal.site.admin_page.buttonUpToParentPage}' hideTitle=['all'] />
			<@aButton href='jsp/admin/site/RemovePage.jsp?page_id=${page.id}' color='danger' buttonIcon='trash' size='' title='#i18n{portal.site.admin_page.buttonDeletePage}' hideTitle=['all']  />
		</@btnGroup>
	</#if>
	<#if extendableResourceActionsHtml?? && extendableResourceActionsHtml?has_content>
		${extendableResourceActionsHtml!}
	</#if>
	<@btnGroup class='ms-1'>
		<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.id}&amp;param_block=2' buttonIcon='wrench me-1' title='#i18n{portal.site.admin_page.labelPageProperty}' hideTitle=['xs','sm','md']  />
	</@btnGroup>
	<@btnGroup class='mx-1'>
		<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.id}&amp;param_block=5'  buttonIcon='plus me-1' title='#i18n{portal.site.admin_page.labelChildPagePage}' hideTitle=['xs','sm','md'] />
	</@btnGroup>
	<@offcanvas id="portlet-type-wrapper" title="#i18n{portal.site.admin_page.labelPortletPage}" btnColor="primary btn-portet" btnIcon="layout-dashboard mx-1" btnTitle="#i18n{portal.site.admin_page.labelPortletPage}" position="end">
	<@listGroup>
	<#list portlet_types_list?sort_by("name") as portlet_type>
		<#if portlet_type.name !=''>
			<@listGroupItem class='p-2' params='data-portlet-type-id="${portlet_type.id}" data-portlet-type-icon="${portlet_type.iconName}" data-portlet-type-href="jsp/admin/DoCreatePortlet.jsp?portlet_type_id=${portlet_type.id}&amp;page_id=${page.id}" data-portlet-type-name="${portlet_type.name}"'>
				<@aButton color='link w-100 justify-content-start btn-portlet' buttonIcon='${portlet_type.iconName} me-2' href='jsp/admin/DoCreatePortlet.jsp?portlet_type_id=${portlet_type.id}&amp;page_id=${page.id}' target='preview' title='${portlet_type.name}' />
			</@listGroupItem>	
		</#if>
	</#list>
	</@listGroup>
	</@offcanvas>
	<@btnGroup class='ms-1'>
		<@button buttonIcon='sitemap' title='Page ${page.name} - Id ${page.id}' hideTitle=['all'] params='data-bs-toggle="offcanvas" data-bs-target="#offcanvasSiteMap" aria-controls="offcanvasSiteMap"' ><@span hide=['all']>#i18n{portal.site.admin_page.tabAdminMapSite}</@span></@button>
	</@btnGroup>
	<@btnGroup class='ms-1'>
		<@aButton href='jsp/site/Portal.jsp?page_id=${page.id}' title='#i18n{portal.site.admin_page.labelShowPage}' buttonIcon='external-link' color='info' class='btn-preview' target='_blank' hideTitle=['all'] />
	</@btnGroup>
	<@btnGroup class='ms-1'>
		<@button id='iframe-fullscreen' color='info' buttonIcon='maximize' />
	</@btnGroup>  
</@btnToolbar>
</#macro>