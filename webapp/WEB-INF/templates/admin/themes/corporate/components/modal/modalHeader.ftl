<#-- Macro: modalHeader
Description: Generates the header content for a Bootstrap modal dialog, with optional parameters for the title level, title text, ID, and additional attributes.
Parameters:
- titleLevel (string, optional): the HTML heading level for the modal title (default is "h4").
- modalTitle (string, optional): the text content of the modal title.
- id (string, optional): the ID of the modal header.
- params (string, optional): additional attributes to add to the modal header, in the form of a string of HTML attributes.
-->
<#macro modalHeader titleLevel='h4' modalTitle='' id='' params=''>
<div class="modal-header"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<${titleLevel} class="modal-title">${modalTitle}</${titleLevel}>
	<#nested>
	<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="#i18n{portal.site.admin_page.buttonClosed}"></button>
</div>
</#macro>