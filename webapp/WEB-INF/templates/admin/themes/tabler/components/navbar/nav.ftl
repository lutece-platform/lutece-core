<#-- Macro: nav
Description: Generates a Bootstrap navigation element, such as a tab or pills.
Parameters:
- tag (string, optional): the HTML tag to use for the navigation element (default is "nav").
- type (string, optional): the type of navigation element (e.g. "tab" or "pill").
- class (string, optional): additional CSS classes to apply to the navigation element.
- id (string, optional): the ID of the navigation element.
- params (string, optional): additional attributes to add to the navigation element, in the form of a string of HTML attributes.
-->
<#macro nav tag='nav' type='tab' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${tag} class="nav nav-${type!}<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</${tag}>
</#macro>