<#--
Macro: cBreadCrumb

Description: Generates a breadcrumb navigation trail for the current page, helping users navigate the site hierarchy.

Parameters:
- home (string, required): Label for the home page link. Default: '#i18n{portal.theme.home}'.
- items (list, required): List of breadcrumb items, each with 'title' and 'url' attributes. Default: ''.
- class (string, optional): Additional CSS class(es) for the breadcrumb nav. Default: ''.
- type (string, optional): If 'fluid', the breadcrumb spans the full page width. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: "Fil d'ariane - @cBreadCrumb"
- bs: components/breadcrumb
- newFeature: false

Snippet:

    Basic usage with a list of items:

    <@cBreadCrumb items=breadcrumbItems />

    Custom home label and fluid layout:

    <@cBreadCrumb home='Home' type='fluid' items=[
        { 'title': 'Services', 'url': 'jsp/site/Portal.jsp?page=services' },
        { 'title': 'Contact', 'url': '' }
    ] />

-->
<#macro cBreadCrumb home='#i18n{portal.theme.home}' items='' class='' containerClass='' listClass='' type='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="#i18n{portal.theme.breadcrumb}" class="breadcrumb-nav<#if class?has_content> ${class!}</#if>"<@commonAttrs params=params />>
	<div class="container<#if type='fluid'>-fluid</#if><#if containerClass?has_content> ${containerClass!}</#if> rounded">
		<ol class="breadcrumb<#if listClass?has_content> ${listClass!}</#if>">
        <#if home?has_content>
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