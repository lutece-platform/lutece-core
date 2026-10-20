<#-- Macro: accordionBody

Description: Defines a macro for generating an accordion-style UI element with expandable and collapsible content.

Parameters:
- id (string, required): the ID of the accordion element
- childId (string, required): the ID of the child element
- class (string, optional): the CSS class of the accordion element
- expanded (boolean, optional): whether the accordion should be expanded by default
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Accordion body with text content:

    <@accordionBody>
        <p>This is the collapsible content area of the accordion panel.</p>
    </@accordionBody>

    Accordion body with a form inside:

    <@accordionBody>
        <@formGroup>
            <@inputGroup>
                <@input type='text' name='username' placeHolder='Enter username' />
            </@inputGroup>
        </@formGroup>
    </@accordionBody>

-->
<#macro accordionBody id=childId class=childClass expanded=expanded params={}>
<div id="${id}" class="accordion-collapse ${class}" aria-labelledby="${childId}-header" data-bs-parent="#${parentId}" <@commonAttrs params=params />>
<@boxBody>
	<#nested>
</@boxBody>
</div>
</div>
</#macro>