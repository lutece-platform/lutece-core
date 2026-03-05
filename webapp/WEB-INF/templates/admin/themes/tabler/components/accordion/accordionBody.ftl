<#-- Macro: accordionBody

Description: Defines a macro for generating an accordion-style UI element with expandable and collapsible content.

Parameters:
- id (string, required): the ID of the accordion element
- childId (string, required): the ID of the child element
- class (string, optional): the CSS class of the accordion element
- expanded (boolean, optional): whether the accordion should be expanded by default
- params (string, optional): additional HTML attributes to include in the accordion element

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
<#macro accordionBody id=childId class=childClass expanded=expanded params=''>
<div id="${id}" class="accordion-collapse ${class}" aria-labelledby="${childId}-header" data-bs-parent="#${parentId}" <#if params!=''> ${params}</#if>>
<@boxBody>
	<#nested>
</@boxBody>
</div>
</#macro>