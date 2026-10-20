<#-- Macro: navItem
Description: Generates a navigation item with the specified link attributes.
Parameters:
- href (string, optional): the URL for the navigation item.
- name (string, optional): the display name for the navigation item.
- active (boolean, optional): whether the navigation item is currently active (default is false).
- title (string, optional): the title attribute for the navigation item.
- alt (string, optional): the alt attribute for the navigation item.
- target (string, optional): the target attribute for the navigation item.
- tag (string, optional): the HTML tag to use for the navigation item (default is "li").
- class (string, optional): the class to use for the navigation item.
- id (string, optional): the ID to use for the navigation item.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Active navigation item with link:

    <@navItem href='jsp/admin/Dashboard.jsp' name='Dashboard' active=true title='Go to dashboard' />

    Navigation item with custom content:

    <@navItem class='dropdown'>
        <a class="dropdown-toggle" data-bs-toggle="dropdown" href="#">Settings</a>
    </@navItem>

-->
<#macro navItem href='' name='' active=false title='' alt='' target='' tag='li' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<${tag} class="nav-link<#if class?has_content> ${class}</#if>"<@commonAttrs id=id params=params />>
	<#if href?has_content >
		<@link class='nav-link' href=href name=name title=title alt=alt target=target active=active /> 
	</#if>
	<#nested>
</#${tag}>
</#macro>