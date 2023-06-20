<#--
Macro: messages

Description: Generates HTML alert elements for displaying error, info, and warning messages.

Parameters:
- errors (list, optional): a list of error messages to display.
- infos (list, optional): a list of info messages to display.
- warnings (list, optional): a list of warning messages to display.
- errors_class (string, optional): the CSS class of the alert element for error messages.
- infos_class (string, optional): the CSS class of the alert element for info messages.
- warnings_class (string, optional): the CSS class of the alert element for warning messages.
-->

<#macro messages errors=[] infos=[] warnings=[] errors_class="alert alert-danger" infos_class="alert alert-info" warnings_class="alert alert-warning">
<#if errors??>
	<#if errors?size gt 0 >
		<#list errors as error >
			<#local errorMessage=error.message />
		</#list>
		<@alert color='danger' title=errorMessage iconTitle='exclamation-circle' dismissible=true id='messages_errors_div'>	</@alert>
	</#if>
</#if>
<#if infos??>
	<#if infos?size gt 0 >
		<#list infos as info >
			<#local infoMessage=info.message />
		</#list>
		<@alert color='info' title=infoMessage iconTitle='info-circle' dismissible=true id='messages_infos_div'></@alert>
	</#if>
</#if>
<#if warnings??>
	<#if warnings?size gt 0 >
		<#list warnings as warning >
			<#local warningMessage=warning.message />
		</#list>
		<@alert color='warning' title=warningMessage iconTitle='exclamation-circle' dismissible=true id='messages_warnings_div'></@alert>
	</#if>
</#if>
</#macro>