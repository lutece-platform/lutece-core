<#--
Macro: breadcrumbItem
Description: Generates an HTML element for a breadcrumb item.
Parameters:
- class (string, optional): the CSS class of the breadcrumb item.
- id (string, optional): the ID of the breadcrumb item element.
- params (string, optional): additional HTML attributes to include in the breadcrumb item element.
-->
<#macro breadcrumbItem class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="breadcrumb-item<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</li>
</#macro>