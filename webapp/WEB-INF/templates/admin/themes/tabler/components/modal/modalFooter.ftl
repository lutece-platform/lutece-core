<#-- Macro: modalFooter
Description: Generates the footer content for a Bootstrap modal dialog, with optional parameters for the ID and additional attributes.
Parameters:
- id (string, optional): the ID of the modal footer.
- params (string, optional): additional attributes to add to the modal footer, in the form of a string of HTML attributes.
-->
<#macro modalFooter id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="modal-footer"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</div>
</#macro>