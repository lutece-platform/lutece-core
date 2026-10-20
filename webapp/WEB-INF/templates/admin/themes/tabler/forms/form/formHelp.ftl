<#-- Macro: formHelp

Description: Generates help text for a Bootstrap form group.

Parameters:
- style (string, optional): the style of the help text (inline or popover).
- class (string, optional): additional CSS classes to add to the help text element.
- labelFor (string, optional): the ID of the input field that the help text is associated with.

Snippet:

    Inline help text below an input:

    <@formHelp labelFor='username'>
        Your username must be between 3 and 20 characters.
    </@formHelp>

    Popover help text:

    <@formHelp style='popover'>
        This field accepts only alphanumeric characters and must be unique across the system.
    </@formHelp>

-->
<#macro formHelp style='inline' class='' labelFor='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if style='inline'>
	<small class="text-muted<#if style!='inline'> form-text</#if><#if class?has_content> ${class}</#if>" <#if labelFor?has_content>id="help_${labelFor}"</#if>>
	<#nested>
	</small>
<#else>
	<#local helpContent><#nested></#local>
	<button type="button" class="btn btn-link p-0 form-help" data-bs-toggle="popover" data-bs-placement="bottom" data-bs-trigger="focus hover" data-bs-html="true" data-bs-content="${helpContent?markup_string?trim}" aria-label="#i18n{portal.util.labelHelp}"<#if labelFor?has_content> aria-describedby="help_${labelFor}"</#if>>?</button>
	<span class="visually-hidden"<#if labelFor?has_content> id="help_${labelFor}"</#if>>${helpContent}</span>
</#if>
</#macro>