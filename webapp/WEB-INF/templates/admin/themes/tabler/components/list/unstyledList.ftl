<#-- Macro: unstyledList

Description: Generates an HTML unordered list (<ul>) with the "unstyled" class, which removes default list styles, and optional attributes.

Parameters:
- id (string, optional): the ID attribute of the unordered list.
- params (string, optional): additional attributes to add to the unordered list, in the form of a string of HTML attributes.
-->
<#macro unstyledList id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="unstyled"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#assign liClass = "margin">
	<#nested>
</ul>
</#macro>