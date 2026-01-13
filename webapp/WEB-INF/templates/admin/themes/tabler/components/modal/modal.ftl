<#-- Macro: modal
Description: Generates a Bootstrap modal dialog, with optional parameters for the ID, additional attributes, background color, and size.
Parameters:
- id (string): the ID of the modal.
- params (string, optional): additional attributes to add to the modal, in the form of a string of HTML attributes.
- bgColor (string, optional): the background color of the modal, using a Bootstrap color class (e.g. "bg-primary").
- size (string, optional): the size of the modal dialog, using a Bootstrap size class (e.g. "lg").
- fullScreen (boolean, optional): whether the modal should be displayed in full screen (default is false).
- vCentered (boolean, optional): whether the modal should be vertically centered (default is true
-->
<#macro modal id params='' bgColor='' size='' fullScreen=false vCentered=true deprecated...>
<@deprecatedWarning args=deprecated />
<div class="modal ${bgColor} fade" id="${id}" <#if params!=''> ${params}</#if>>
	<div class="modal-dialog<#if size!=''> modal-${size}</#if><#if fullScreen> modal-fullscreen</#if><#if vCentered> modal-dialog-centered</#if>">
		<div class="modal-content">
			<#nested>
		</div>
	</div>
</div>
</#macro>