<#--
Macro: callOut

Description: Generates an HTML callout element with an optional icon and title.

Parameters:
- color (string, required): the background color of the callout element.
- titleLevel (string, optional): the level of the title element (h1, h2, h3, h4, h5, or h6).
- title (string, optional): the title of the callout element.
- callOutIcon (string, optional): the name of the icon to include in the callout element.
- id (string, optional): the ID of the callout element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the callout element.
-->
<#macro callOut color='' titleLevel='h3' title='' callOutIcon='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="alert alert-important alert-${color}"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#if title!=''><${titleLevel} class='mb-0'><@icon style=callOutIcon /> ${title}</${titleLevel}></#if>
	<#nested>
</div>
</#macro>