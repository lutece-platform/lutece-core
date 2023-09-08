<#-- Macro: modal
Description: Generates a Bootstrap modal dialog, with optional parameters for the ID, additional attributes, background color, and size.
Parameters:
- id (string): the ID of the modal.
- params (string, optional): additional attributes to add to the modal, in the form of a string of HTML attributes.
- bgColor (string, optional): the background color of the modal, using a Bootstrap color class (e.g. "bg-primary").
- size (string, optional): the size of the modal dialog, using a Bootstrap size class (e.g. "lg").
-->
<#macro modal id params='' bgColor='' size=''>
<div class="modal ${bgColor} fade" id="${id}" <#if params!=''> ${params}</#if>>
	<div class="modal-dialog<#if size!=''> modal-${size}</#if>" role="document">
		<div class="modal-content">
			<#nested>
		</div>
	</div>
</div>
</#macro>