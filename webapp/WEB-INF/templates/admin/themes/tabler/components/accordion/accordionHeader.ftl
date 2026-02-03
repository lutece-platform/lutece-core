<#--
Macro: accordionHeader

Description: Generates an HTML header element for an accordion-style UI element with expandable and collapsible content.

Parameters:
- id (string, optional): the ID of the header element. If not provided, a default ID will be generated.
- title (string, required): the title of the header element.
- parentId (string, required): the ID of the parent element.
- childId (string, required): the ID of the child element.
- boxTools (boolean, optional): whether to include box tools for the header element.
- params (string, optional): additional HTML attributes to include in the header element.
- headerIcon (string, optional): the name of the icon to include in the header element.
-->
<#macro accordionHeader id='' title='' class='' parentId=parentId childId=childId boxTools=false params='' headerIcon='' >
<div class="accordion-item">
	<h2 class="accordion-header ${class}" id="${childId}-header"<#if params!=''> ${params}</#if>>
		<button class="accordion-button<#if aClass!=''> ${aClass}</#if>" type="button" data-bs-toggle="collapse" data-bs-target="#${childId}" aria-expanded="${expanded}" aria-controls="${childId}">
		<#if headerIcon!=''><@icon style=headerIcon /></#if><span class="ms-2">${title}</span>
	</h2>
	<#local nested><#nested></#local>
	<#if nested?has_content><#if boxTools><div class="box-tools"></#if>${nested}<#if boxTools></div></#if></#if>
</div>
<#assign parentId = parentId />
</#macro>