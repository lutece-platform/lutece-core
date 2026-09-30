<#--
Macro: adminContentHeader
Description: Generates a header section for an administrative page, including a page title and optional documentation link.
Parameters:
- feature_title (string, required): the title of the feature or section of the application.
- feature_url (string, optional): the URL of the feature or section of the application.
- page_title (string, optional): the title of the current page.
- page_breadcrumbs (list of BreadcrumbItem, optional): the ancestors of the current page, displayed as breadcrumbs between the feature title and the page title. Each item has a title and an optional url.
-->
<#macro adminContentHeader>
<header class="page-header d-print-none border-bottom px-2 px-md-4 d-flex align-items-center h-60 navbar navbar-expand-lg overflow-y-hidden">
 <@div class="row align-items-center admin-site-toolbar w-100">
      <@div class="col">
            <@div class="page-pretitle" id="feature-title">
            <#if page_breadcrumbs?has_content>
                <@breadcrumbs class='mb-0 fw-bold'>
                    <@breadcrumbItem><#if feature_url??><@link href='${feature_url}' title='${feature_title!""}'>${feature_title!''}</@link><#else>${feature_title!''}</#if></@breadcrumbItem>
                    <#list page_breadcrumbs as item>
                        <@breadcrumbItem><#if item.url?has_content><@link href='${item.url}'>${item.title!''}</@link><#else>${item.title!''}</#if></@breadcrumbItem>
                    </#list>
                    <#if page_title?has_content>
                        <@breadcrumbItem class='active' params='aria-current="page"'>${page_title}</@breadcrumbItem>
                    </#if>
                </@breadcrumbs>
            <#else>
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
            </#if>
            </@div>
      </@div>
      <@div id="page-header-buttons" class="col-auto ms-auto d-print-none">
            <@adminHeaderDocumentationLink />
      </@div>
</@div>
</header>
</#macro>