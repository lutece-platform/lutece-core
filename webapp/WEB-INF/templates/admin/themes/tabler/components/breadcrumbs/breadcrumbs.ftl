<#--
Macro: breadcrumbs

Description: Generates an HTML element for breadcrumbs.

Parameters:
- id (string, optional): the ID of the breadcrumbs element.
- class (string, optional): the CSS class of the breadcrumbs element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro breadcrumbs id='' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="breadcrumb">
	<ol class="breadcrumb<#if class?has_content> ${class}</#if>"<@commonAttrs id=id params=params />>
		<#nested>
	</ol>
</nav>
</#macro>