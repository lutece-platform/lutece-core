<#--
Macro: adminContentHeader
Description: Generates a header section for an administrative page, including a page title and optional documentation link.
Parameters:
- feature_title (string, required): the title of the feature or section of the application.
- feature_url (string, optional): the URL of the feature or section of the application.
- page_title (string, optional): the title of the current page.
-->
<#macro adminContentHeader>
<div class="page-header d-print-none" id="admin-content-header" aria-label="Page header">
    <div class="container-xl">
        <div class="row g-2 align-items-center">
            <div class="col">
                <!-- Page pre-title -->
                <div class="page-pretitle" id="feature-title"><#if feature_url??><@link href='${feature_url}' title='${feature_title!""}'>${feature_title!''}</@link><#else>${feature_title!''}</#if></div>
                <#if page_title?has_content && page_title != feature_title><h2 class="page-title">${page_title!''}</h2></#if>
            </div>
            <div class="page-header-buttons col-auto ms-auto d-print-none"></div>
        </div>
    </div>
</div>
<!-- END PAGE HEADER -->
 <@adminHeaderDocumentationLink />
</#macro>