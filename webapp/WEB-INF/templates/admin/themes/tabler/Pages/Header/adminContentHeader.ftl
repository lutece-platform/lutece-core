<#--
Macro: adminContentHeader
Description: Generates a header section for an administrative page, including a page title and optional documentation link.
Parameters:
- feature_title (string, required): the title of the feature or section of the application.
- feature_url (string, optional): the URL of the feature or section of the application.
- page_title (string, optional): the title of the current page.
-->
<#macro adminContentHeader>
<header class="page-header d-print-none">
	<@div class="row align-items-center admin-site-toolbar mx-0">
		<@div class="col">
			<@div class="page-pretitle" id="feature-title">
				<#if feature_url??><@link href='${feature_url}' title='${feature_title!""}'>${feature_title!''}</@link><#else>${feature_title!''}</#if>
			</@div>
			<#if page_title?has_content><h2>${page_title}</h2></#if>
		</@div>
		<@div id="page-header-buttons" class="col-auto ms-auto d-print-none"> 
			<@adminHeaderDocumentationLink />
		</@div>
	</@div>
</header>
<section class="content <#if feature_url?? && feature_url?ends_with('AdminSite.jsp')>no-padding</#if> min-vh-100">
</#macro>