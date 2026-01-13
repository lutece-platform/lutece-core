<#--
Macro: breadcrumbs

Description: Generates an HTML element for breadcrumbs.

Parameters:
- id (string, optional): the ID of the breadcrumbs element.
- class (string, optional): the CSS class of the breadcrumbs element.
- params (string, optional): additional HTML attributes to include in the breadcrumbs element.
-->
<#macro breadcrumbs id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="breadcrumb">
	<ol class="breadcrumb<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
		<#nested>
	</ol>
</nav>
</#macro>