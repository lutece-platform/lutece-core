<#--
Macro: breadcrumbItem
Description: Generates an HTML element for a breadcrumb item.
Parameters:
- class (string, optional): the CSS class of the breadcrumb item.
- id (string, optional): the ID of the breadcrumb item element.
- params (string, optional): additional HTML attributes to include in the breadcrumb item element.

Snippet:

    Breadcrumb item with a link:

    <@breadcrumbItem>
        <a href='/dashboard'>Dashboard</a>
    </@breadcrumbItem>

    Active breadcrumb item (current page):

    <#assign paramsAttr1>aria-current="page"</#assign>
    <@breadcrumbItem class='active' params=paramsAttr1>
        User Profile
    </@breadcrumbItem>

-->
<#macro breadcrumbItem class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="breadcrumb-item<#if class?has_content> ${class}</#if>"<#if id?has_content> id="${id}"</#if><#if params?has_content> ${params}</#if>>
	<#nested>
</li>
</#macro>