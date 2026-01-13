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
- params (string, optional): additional parameters to include in the navigation item.
-->
<#macro navItem href='' name='' active=false title='' alt='' target='' tag='li' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${tag} class="nav-link<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#if href !='' >
		<@link class='nav-link' href=href name=name title=title alt=alt target=target active=active /> 
	</#if>
	<#nested>
</#${tag}>
</#macro>