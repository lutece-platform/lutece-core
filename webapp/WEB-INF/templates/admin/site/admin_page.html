<@row>
  	<@columns>
    	<@box>
			<#assign title>
				<@button type='button' id='fullscreen' params='data-toggle="tooltip" data-placement="bottom"' title='#i18n{portal.site.admin_page.buttonFullscreen}' buttonIcon='arrows-alt fa-fw' hideTitle=['all'] color='secondary' size='sm' />
				<@link href='jsp/admin/site/AdminSite.jsp?page_id=${page.id}' title='${page.name}'>
					<@tag color='primary' title='${page.id} - ${page.name}'>${page.id}</@tag> ${page.name}
				</@link>
			</#assign>
			<@boxHeader titleLevel='p' title=title boxTools=true>
				<@btnToolbar>
				
					<@btnGroup hide=['xs']>
						<@aButton href='#' id='display-full' params='data-toggle="tooltip" data-placement="bottom"' title='Ecran large' buttonIcon='desktop fa-fw' color='secondary' size='sm' hideTitle=['all'] />
						<@aButton href='#' id='display-940' params='data-toggle="tooltip" data-placement="bottom"' title='Tablette - 940px' buttonIcon='tablet fa-fw' color='secondary' size='sm' hideTitle=['all'] />
						<@aButton href='#' id='display-480' params='data-toggle="tooltip" data-placement="bottom"' title='Smartphone - 480px' buttonIcon='mobile fa-fw' color='secondary' size='sm' hideTitle=['all'] />
					</@btnGroup>
					
					<#if page.id != 1>
					<@btnGroup>
						<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.parentPageId}' color='success' buttonIcon='arrow-up' size='sm' title='#i18n{portal.site.admin_page.buttonUpToParentPage}' hideTitle=['all'] />
						<@aButton href='jsp/admin/site/RemovePage.jsp?page_id=${page.id}' color='danger' buttonIcon='trash' size='sm' title='#i18n{portal.site.admin_page.buttonDeletePage}' hideTitle=['all'] />
					</@btnGroup>
					</#if>

					<@btnGroup hide=['xs'] align='right'>
					<@tform type='inline' action='jsp/admin/site/AdminSite.jsp' role='search'>
						<@formGroup formStyle='inline' labelFor='page_id' labelKey='${i18n("portal.site.admin_page.buttonSearchPage")}' hideLabel=['all']>
							<@inputGroup>
								<@input type='text' name='page_id' id='page_id' placeHolder='${i18n("portal.site.admin_page.buttonSearchPage")}' size='sm' />
								<@inputGroupItem type='btn' pos='append'>
									<@button type='submit' color='default' title='${i18n("portal.site.admin_page.buttonSearchPage")}' hideTitle=['all'] buttonIcon='search' size='sm' />
								</@inputGroupItem>
							</@inputGroup>
						</@formGroup>
						<#if page_message!=""><p><@badge type="label" style="important">${page_message}</@badge></p></#if>
					</@tform>
					</@btnGroup>
					
					<@btnGroup>
						<@button title='#i18n{portal.site.admin_page.labelPortletPage}' size='sm' dropdownMenu=true id='portlet-type' buttonIcon='th-large' id='portlet' hideTitle=['xs','sm','md']>
						<#if portlet_types_list?has_content>
							<#list portlet_types_list?sort_by("name") as portlet_type>
								<@dropdownItem class='portlet-type-ref' href='jsp/admin/DoCreatePortlet.jsp?portlet_type_id=${portlet_type.id}&amp;page_id=${page.id}' title='${portlet_type.name}'>
									${portlet_type.name}
								</@dropdownItem>
							</#list>
						</#if>
						</@button>

						<#if extendableResourceActionsHtml?? && extendableResourceActionsHtml?has_content>
							${extendableResourceActionsHtml!}
						</#if>
						
						<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.id}&amp;param_block=2' buttonIcon='wrench' size='sm' title='#i18n{portal.site.admin_page.labelPageProperty}' hideTitle=['xs','sm','md'] />
						<@aButton href='jsp/admin/site/AdminSite.jsp?page_id=${page.id}&amp;param_block=5'  buttonIcon='plus' size='sm' title='#i18n{portal.site.admin_page.labelChildPagePage}' hideTitle=['xs','sm','md'] />
						<@aButton href='jsp/admin/site/AdminMap.jsp?page_id=${page.id }' buttonIcon='sitemap' size='sm' title='Page ${page.name} - Id ${page.id}' hideTitle=['all']><@span hide=['xs','sm','md']>#i18n{portal.site.admin_page.tabAdminMapSite}</@span></@aButton>
					</@btnGroup>
				</@btnToolbar>
			</@boxHeader>
		</@box>
			
		<div id='admin-page-preview'>
			${page_block}
			<!-- HTML in jsp/admin/site/AdminSite.jsp -->
		</div>
	</@columns>
</@row>