<#-- Macro: modal
Description: Generates a Bootstrap modal dialog, with optional parameters for the ID, additional attributes, background color, and size.
Parameters:
- id (string): the ID of the modal.
- params (string, optional): additional attributes to add to the modal, in the form of a string of HTML attributes.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
- bgColor (string, optional): the background color of the modal, using a Bootstrap color class (e.g. "bg-primary").
- size (string, optional): the size of the modal dialog, using a Bootstrap size class (e.g. "lg").
- fullScreen (boolean, optional): whether the modal should be displayed in full screen (default is false).
- vCentered (boolean, optional): whether the modal should be vertically centered (default is true

Snippet:

    Basic modal dialog:

    <@modal id='confirmDialog'>
        <@modalHeader modalTitle='Confirmation' />
        <@modalBody>
            <p>Are you sure you want to proceed?</p>
        </@modalBody>
        <@modalFooter>
            <button type='button' class='btn btn-secondary' data-bs-dismiss='modal'>Cancel</button>
            <button type='button' class='btn btn-primary'>Confirm</button>
        </@modalFooter>
    </@modal>

    Large full-screen modal with custom background:

    <@modal id='detailModal' size='lg' fullScreen=true bgColor='bg-light'>
        <@modalHeader modalTitle='Details' titleLevel='h3' />
        <@modalBody>
            <p>Full-screen content goes here.</p>
        </@modalBody>
    </@modal>

    Small non-centered modal with extra attributes:

    <@modal id='alertModal' size='sm' vCentered=false params='data-bs-backdrop="static"'>
        <@modalHeader modalTitle='Alert' />
        <@modalBody>
            <p>This modal cannot be dismissed by clicking outside.</p>
        </@modalBody>
    </@modal>

-->
<#macro modal id params='' attrs={} bgColor='' size='' fullScreen=false vCentered=true deprecated...>
<@deprecatedWarning args=deprecated />
<div class="modal ${bgColor} fade" id="${id}" <@commonAttrs params=params attrs=attrs />>
	<div class="modal-dialog<#if size?has_content> modal-${size}</#if><#if fullScreen> modal-fullscreen</#if><#if vCentered> modal-dialog-centered</#if>">
		<div class="modal-content">
			<#nested>
		</div>
	</div>
</div>
</#macro>