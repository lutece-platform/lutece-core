<#-- Macro: modalBody

Description: Generates the body content for a Bootstrap modal dialog, with optional parameters for the ID and additional attributes.

Parameters:
- id (string, optional): the ID of the modal body.
- params (string, optional): additional attributes to add to the modal body, in the form of a string of HTML attributes.

Snippet:

    Basic modal body with text content:

    <@modalBody>
        <p>This is the content of the modal body.</p>
    </@modalBody>

    Modal body with ID and additional attributes:

    <@modalBody id='formBody' params='data-step="1"'>
        <form>
            <input type='text' class='form-control' name='username' placeholder='Enter username' />
        </form>
    </@modalBody>

-->
<#macro modalBody id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="modal-body"<#if id?has_content> id="${id}"</#if><#if params?has_content> ${params}</#if>>
	<#nested>
</div>
</#macro>