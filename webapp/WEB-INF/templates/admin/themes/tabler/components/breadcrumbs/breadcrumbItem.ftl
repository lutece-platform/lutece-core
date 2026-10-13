<#--
Macro: breadcrumbItem
Description: Generates an HTML element for a breadcrumb item.
Parameters:
- class (string, optional): the CSS class of the breadcrumb item.
- id (string, optional): the ID of the breadcrumb item element.
- params (string, optional): additional HTML attributes to include in the breadcrumb item element.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Breadcrumb item with a link:

    <@breadcrumbItem>
        <a href='/dashboard'>Dashboard</a>
    </@breadcrumbItem>

    Active breadcrumb item (current page):

    <@breadcrumbItem class='active' attrs={'aria-current': "page"}>
        User Profile
    </@breadcrumbItem>

-->
<#macro breadcrumbItem class='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<li class="breadcrumb-item<#if class?has_content> ${class}</#if>"<@commonAttrs id=id params=params attrs=attrs />>
	<#nested>
</li>
</#macro>