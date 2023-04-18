<#--
Macro: adminContentHeader
Description: Generates a header section for an administrative page, including a page title and optional documentation link.
Parameters:
- feature_title (string, required): the title of the feature or section of the application.
- feature_url (string, optional): the URL of the feature or section of the application.
- page_title (string, optional): the title of the current page.
-->
<#macro adminContentHeader>
<header class="page-header d-print-none border-bottom px-4 d-flex align-items-center h-60 navbar navbar-expand-lg">
 <@div class="row align-items-center admin-site-toolbar w-100">
      <@div class="col">
      <@div class="page-pretitle" id="feature-title">
	  <span class="mb-0 fw-bold">
      <#if feature_url?? >
      <@link href='${feature_url}' title='${feature_title!""}'>${feature_title!''}</@link> >
      <#else>
      ${feature_title!''}
      </#if>
      <#if page_title?has_content>
      ${page_title!''}
      <#else>
      ${feature_title!''}
      </#if>
	  </span>
	  </@div>
      </@div>
      <@div id="page-header-buttons" class="col-auto ms-auto d-print-none">
      <@adminHeaderDocumentationLink />
      </@div>
      </@div>
</header>
</#macro>