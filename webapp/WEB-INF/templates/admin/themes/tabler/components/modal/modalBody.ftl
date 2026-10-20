<#-- Macro: modalBody

Description: Generates the body content for a Bootstrap modal dialog, with optional parameters for the ID and additional attributes.

Parameters:
- id (string, optional): the ID of the modal body.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic modal body with text content:

    <@modalBody>
        <p>This is the content of the modal body.</p>
    </@modalBody>

    Modal body with ID and additional attributes:

    <@modalBody id='formBody' params={'data-step': "1"}>
        <form>
            <input type='text' class='form-control' name='username' placeholder='Enter username' />
        </form>
    </@modalBody>

-->
<#macro modalBody id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<div class="modal-body"<@commonAttrs id=id params=params />>
	<#nested>
</div>
</#macro>