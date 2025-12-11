<#-- Macro: formHelp

Description: Generates help text for a Bootstrap form group.

Parameters:
- style (string, optional): the style of the help text (inline or popover).
- class (string, optional): additional CSS classes to add to the help text element.
- labelFor (string, optional): the ID of the input field that the help text is associated with.

-->
<#macro formHelp style='inline' class='' labelFor='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if style='inline'>
	<small class="text-muted<#if style!='inline'> form-text</#if><#if class!=''> ${class}</#if>" <#if labelFor!=''>aria-describedby="${labelFor}"</#if>>
	<#nested>
	</small>
<#else>
	<span class="form-help" data-bs-toggle="popover" data-bs-placement="bottom" data-bs-trigger="hover" data-bs-html="true" data-bs-content="<#nested>">?</span>
</#if>
</#macro>