<#--
Macro: breadcrumbItem
Description: Generates an HTML element for a breadcrumb item.
Parameters:
- class (string, optional): the CSS class of the breadcrumb item.
- id (string, optional): the ID of the breadcrumb item element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Breadcrumb item with a link:

    <@breadcrumbItem>
        <a href='/dashboard'>Dashboard</a>
    </@breadcrumbItem>

    Active breadcrumb item (current page):

    <@breadcrumbItem class='active' params={'aria-current': "page"}>
        User Profile
    </@breadcrumbItem>

-->
<#macro breadcrumbItem class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<li class="breadcrumb-item<#if class?has_content> ${class}</#if>"<@commonAttrs id=id params=params />>
	<#nested>
</li>
</#macro>