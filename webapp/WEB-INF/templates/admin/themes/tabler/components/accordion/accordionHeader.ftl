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
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
- headerIcon (string, optional): the name of the icon to include in the header element.

Snippet:

    Simple accordion header with title:

    <@accordionHeader title='General Information' />

    Accordion header with icon and box tools:

    <@accordionHeader title='Configuration' headerIcon='settings' boxTools=true>
        <@aButton href='#' buttonIcon='edit' size='sm' />
    </@accordionHeader>

-->
<#macro accordionHeader id='' title='' class='' parentId=parentId childId=childId boxTools=false params='' attrs={} headerIcon='' >
<div class="accordion-item">
	<h2<@commonAttrs id=childId + '-header' classBase='accordion-header' class=class params=params attrs=attrs />>
		<button class="accordion-button<#if aClass!=''> ${aClass}</#if>" type="button" data-bs-toggle="collapse" data-bs-target="#${childId}" aria-expanded="${expanded}" aria-controls="${childId}">
		<#if headerIcon!=''><@icon style=headerIcon /></#if><span class="ms-2">${title}</span>
        <div class="accordion-button-toggle">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true" focusable="false" class="icon"><path d="M6 9l6 6l6 -6"></path></svg>
        </div>
	</h2>
	<#local nested><#nested></#local>
	<#if nested?has_content><#if boxTools><div class="box-tools"></#if>${nested}<#if boxTools></div></#if></#if>
<#assign parentId = parentId />
</#macro>