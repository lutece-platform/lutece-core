<#-- Macro: navBar
Description: Generates a  navigation bar with the specified tag, class, ID, and parameters.
Parameters:
- tag (string, optional): the HTML tag to use for the navigation bar (default is "nav").
- class (string, optional): the class to use for the navigation bar.
- id (string, optional): the ID to use for the navigation bar.
- params (string, optional): additional parameters to include in the navigation bar.
-->
<#macro navBar tag='nav' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${tag} class="navbar<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</${tag}>
</#macro>