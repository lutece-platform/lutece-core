<#-- Macro: adminSiteToolbar
Description: Generates a toolbar for use in the site admin panel. It generates a Bootstrap toolbar that includes buttons for various site management tasks, such as searching for a page, changing the display size, deleting a page, adding a child page, and viewing the site map.

Snippet:

    Display the site admin toolbar:

    <@adminSiteToolbar />

-->
<#macro adminSiteToolbar >
<@columns class='d-none d-lg-block flex-1'>
	<@btnToolbar>
		<@btnGroup class='ms-3 me-2'>
			<@button id='display-desktop' title='#i18n{portal.site.admin_page.buttonXLargeScreen}' buttonIcon='device-desktop' color='primary' class='btn-preview active' hideTitle=['all'] />
			<@button id='display-laptop' title='#i18n{portal.site.admin_page.buttonLargeScreen}' buttonIcon='device-laptop' color='primary' class='btn-preview' hideTitle=['all'] />
			<@button id='display-tablet' title='#i18n{portal.site.admin_page.buttonTablet}' buttonIcon='device-tablet' color='primary' class='btn-preview' hideTitle=['all'] />
			<@button id='display-phone' title='#i18n{portal.site.admin_page.buttonSmartphone}' buttonIcon='device-mobile' color='primary' class='btn-preview' hideTitle=['all'] />
		</@btnGroup>
		</@btnToolbar>
</@columns>
<@columns class='col-auto'>
	<@btnToolbar>
		<@btnGroup class='ms-1 me-2'>
			<#if page.id != 1>
			<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.parentPageId}' color='success' buttonIcon='arrow-up' size='' title='#i18n{portal.site.admin_page.buttonUpToParentPage}' hideTitle=['all'] />
			<@aButton href='jsp/admin/site/RemovePage.jsp?page_id=${page.id}' color='danger' buttonIcon='trash' size='' title='#i18n{portal.site.admin_page.buttonDeletePage}' hideTitle=['all']  />
			</#if>
			<#local targetUrlEnt1>jsp/admin/site/AdminSite.jsp?page_id=${page.id}&amp;param_block=2</#local>
			<@offcanvas id="page-properties" redirectForm=false title="#i18n{portal.site.admin_page.titlePageProperties}" targetUrl=targetUrlEnt1 targetElement='#properties' btnColor="portet" btnIcon="wrench mx-1" btnTitle="#i18n{portal.site.admin_page.titlePageProperties}" hideTitle=['all'] size="" />
			<#local targetUrlEnt2>jsp/admin/site/AdminSite.jsp?page_id=${page.id}&amp;param_block=5</#local>
			<@offcanvas id="childpage-properties" title="#i18n{portal.site.admin_page.titleAddChildPage}" targetUrl=targetUrlEnt2 targetElement='#childpage' btnColor="portet" btnIcon="file-plus me-1" btnTitle="#i18n{portal.site.admin_page.titleAddChildPage}" hideTitle=['all'] size=""  redirectForm=false/>
			<@offcanvas id="portlet-type-wrapper" title="#i18n{portal.site.admin_page.labelPortletPage}" btnColor="portet" btnIcon="apps" btnTitle="#i18n{portal.site.admin_page.labelPortletPage}" hideTitle=['all'] >
			<@listGroup>
				<#list portlet_types_list?sort_by("name") as portlet_type>
					<#if portlet_type.name?has_content>
						<#assign iconPortlet><#if portlet_type.iconName??>${portlet_type.iconName!'puzzle'}<#else>puzzle</#if></#assign>
<#assign iconPortlet = iconPortlet?is_markup_output?then(iconPortlet?markup_string, iconPortlet) />
						<#local paramsEnt3>data-portlet-type-id="${portlet_type.id}" data-portlet-type-icon="${iconPortlet!}" data-portlet-type-href="jsp/admin/DoCreatePortlet.jsp?portlet_type_id=${portlet_type.id}&amp;page_id=${page.id}" data-portlet-type-name="${portlet_type.name}"</#local>
						<@listGroupItem class='p-2' params=paramsEnt3>
							<#local hrefEnt4>jsp/admin/DoCreatePortlet.jsp?portlet_type_id=${portlet_type.id}&amp;page_id=${page.id}</#local>
							<@aButton color='link w-100 btn-portlet d-flex justify-content-start' buttonIcon='${iconPortlet!} me-2 me-2' href=hrefEnt4 target='preview' title='${portlet_type.name}' />
						</@listGroupItem>	
					</#if>
				</#list>
			</@listGroup>
			</@offcanvas>
			<#if extendableResourceActionsHtml?? && extendableResourceActionsHtml?has_content>${extendableResourceActionsHtml!}</#if>
			<#local paramsAttr1>data-bs-toggle="offcanvas" data-bs-target="#offcanvasSiteMap" aria-controls="offcanvasSiteMap"</#local>
			<@button class='d-block d-md-none' buttonIcon='sitemap' title='Page ${page.name} - Id ${page.id}' hideTitle=['all'] params=paramsAttr1 >
				<@span hide=['all']>#i18n{portal.site.admin_page.tabAdminMapSite}</@span>
			</@button>
		</@btnGroup>
	</@btnToolbar>
</@columns>
<@columns class='col-auto'>
	<@btnToolbar>
		<@btnGroup class='ms-1'>
			<@aButton href='jsp/site/Portal.jsp?page_id=${page.id}' title='#i18n{portal.site.admin_page.labelShowPage}' buttonIcon='external-link' color='info' class='btn-preview' target='_blank' hideTitle=['all'] />
			<@button id='iframe-fullscreen' color='info' buttonIcon='maximize' />
		</@btnGroup>  
	</@btnToolbar>
</@columns>
<@columns class='col-auto'>
	<@btnToolbar class='d-none d-md-block'>
		<@tform type='' class='justify-content-end d-flex' id='search-page-id' action='jsp/admin/site/AdminSite.jsp' role='search'>
			<@inputGroup>
				<@input type='number' name='page_id' id='page_id' min=1 title='${i18n("portal.site.admin_page.buttonSearchPage")}' value=page.id!  pattern='\\d' />
				<@button type='submit' color='primary' title='${i18n("portal.site.admin_page.buttonSearchPage")}' hideTitle=['all'] buttonIcon='search' />
				<#local paramsAttr2>data-bs-toggle="offcanvas" data-bs-target="#offcanvasSiteMap" aria-controls="offcanvasSiteMap"</#local>
				<@button buttonIcon='sitemap' title='Page ${page.name} - Id ${page.id}' hideTitle=['all'] params=paramsAttr2 >
					<@span hide=['all']>#i18n{portal.site.admin_page.tabAdminMapSite}</@span>
				</@button>
			</@inputGroup>
		</@tform>
	</@btnToolbar>
</@columns>
</#macro>
