<#-- Macro: nav
Description: Generates a Bootstrap navigation element, such as a tab or pills.
Parameters:
- tag (string, optional): the HTML tag to use for the navigation element (default is "nav").
- type (string, optional): the type of navigation element (e.g. "tab" or "pill").
- class (string, optional): additional CSS classes to apply to the navigation element.
- id (string, optional): the ID of the navigation element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic tab navigation:

    <@nav type='tab' id='mainNav'>
        <@navItem href='#home' name='Home' active=true />
        <@navItem href='#profile' name='Profile' />
    </@nav>

    Pill navigation with custom class:

    <@nav type='pill' class='justify-content-center'>
        <@navItem href='#dashboard' name='Dashboard' active=true />
        <@navItem href='#reports' name='Reports' />
    </@nav>

-->
<#macro nav tag='nav' type='tab' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<${tag} class="nav nav-${type!}<#if class?has_content> ${class}</#if>"<@commonAttrs id=id params=params />>
	<#nested>
</${tag}>
</#macro>