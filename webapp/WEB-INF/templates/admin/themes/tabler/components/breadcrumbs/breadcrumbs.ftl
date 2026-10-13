<#--
Macro: breadcrumbs

Description: Generates an HTML element for breadcrumbs.

Parameters:
- id (string, optional): the ID of the breadcrumbs element.
- class (string, optional): the CSS class of the breadcrumbs element.
- params (string, optional): additional HTML attributes to include in the breadcrumbs element.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic breadcrumb navigation:

    <@breadcrumbs>
        <@breadcrumbItem><a href='/'>Home</a></@breadcrumbItem>
        <@breadcrumbItem><a href='/users'>Users</a></@breadcrumbItem>
        <@breadcrumbItem class='active'>Edit User</@breadcrumbItem>
    </@breadcrumbs>

    Breadcrumbs with custom ID and class:

    <@breadcrumbs id='admin-breadcrumb' class='bg-light p-2 rounded'>
        <@breadcrumbItem><a href='/admin'>Dashboard</a></@breadcrumbItem>
        <@breadcrumbItem class='active'>Settings</@breadcrumbItem>
    </@breadcrumbs>

-->
<#macro breadcrumbs id='' class='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="breadcrumb">
	<ol class="breadcrumb<#if class?has_content> ${class}</#if>"<@commonAttrs id=id params=params attrs=attrs />>
		<#nested>
	</ol>
</nav>
</#macro>