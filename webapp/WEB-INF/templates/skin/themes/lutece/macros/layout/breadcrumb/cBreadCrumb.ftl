<#--
Macro: cBreadCrumb

Description: Generates a breadcrumb navigation trail for the current page, helping users navigate the site hierarchy.

Parameters:
- home (string, required): Label for the home page link. Default: '#i18n{portal.theme.home}'.
- items (list, required): List of breadcrumb items, each with 'title' and 'url' attributes. Default: ''.
- class (string, optional): Additional CSS class(es) for the breadcrumb nav. Default: ''.
- type (string, optional): If 'fluid', the breadcrumb spans the full page width. Default: ''.
- params (string, optional): Additional HTML attributes for the breadcrumb nav element. Default: ''.

Snippet:

    Basic usage with a list of items:

    <@cBreadCrumb items=breadcrumbItems />

    Custom home label and fluid layout:

    <@cBreadCrumb home='Home' type='fluid' items=[
        { 'title': 'Services', 'url': 'jsp/site/Portal.jsp?page=services' },
        { 'title': 'Contact', 'url': '' }
    ] />

-->
<#macro cBreadCrumb home='#i18n{portal.theme.home}' items='' class='' type='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="#i18n{portal.theme.breadcrumb}" class="breadcrumb-nav<#if class!=''> ${class!}</#if>"<#if params!=''> ${params!}</#if>>
	<div class="container<#if type='fluid'>-fluid</#if> bg-light rounded">
		<ol class="breadcrumb bg-light py-2 px-4 ">
        <#if home!=''>
            <li class="breadcrumb-item">
                <a target="_top" href=".">${home!}</a>
            </li>
        </#if>
        <#if items?has_content>
            <#assign iMax=items?size>
            <#assign idx=1>
            <#list items as i>
                <li class="breadcrumb-item <#if iMax==idx>active</#if>"<#if iMax==idx> aria-current="page"</#if>>
                    <#if idx lt iMax>
                    <a href="${i.url!}" title="${i.title!}" target="_top">
                    </#if>
                    ${i.title!}
                    <#if idx lt iMax>
                    </a>
                    </#if>
                </li>
                <#assign idx+=1>
            </#list>
        </#if>
        <#nested> 
        </ol>
	</div>
</nav>
</#macro>