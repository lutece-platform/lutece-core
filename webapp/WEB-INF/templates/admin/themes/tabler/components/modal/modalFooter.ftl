<#-- Macro: modalFooter
Description: Generates the footer content for a Bootstrap modal dialog, with optional parameters for the ID and additional attributes.
Parameters:
- id (string, optional): the ID of the modal footer.
- params (string, optional): additional attributes to add to the modal footer, in the form of a string of HTML attributes.

Snippet:

    Modal footer with action buttons:

    <@modalFooter>
        <button type='button' class='btn btn-secondary' data-bs-dismiss='modal'>Close</button>
        <button type='button' class='btn btn-primary'>Save changes</button>
    </@modalFooter>

    Modal footer with ID and custom attributes:

    <@modalFooter id='wizardFooter' params='data-step="final"'>
        <button type='button' class='btn btn-outline-secondary'>Previous</button>
        <button type='submit' class='btn btn-success'>Submit</button>
    </@modalFooter>

-->
<#macro modalFooter id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="modal-footer"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</div>
</#macro>