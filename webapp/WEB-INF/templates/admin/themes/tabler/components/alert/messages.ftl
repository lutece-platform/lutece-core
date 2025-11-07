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
		<@alert color='danger' title='#i18n{portal.util.message.titleError}' iconTitle='exclamation-circle' dismissible=true id='messages_errors_div'>
		<@unstyledList>
		<#list errors as error ><#if error.message??><@li>${error.message!' #i18n{portal.util.message.titleError} '}</@li></#if></#list>
		</@unstyledList>
		</@alert>
	</#if>
</#if>
<#if warnings??>
	<#if warnings?size gt 0 >
	<#assign first = warnings[0] />
		<@alert color='warning' title='#i18n{portal.util.message.titleWarning}' iconTitle='exclamation-circle' dismissible=true id='messages_warnings_div'>
			<@unstyledList>
			 <#if first?is_string>
				<#list warnings as warning ><@li>${warning!' #i18n{portal.util.message.titleWarning} '}</@li></#list>
	        <#else >
	        	<#list warnings as warning ><#if warning.message??><@li>${warning.message!' #i18n{portal.util.message.titleWarning} '}</@li></#if></#list>
	        </#if>
			</@unstyledList>
		</@alert>
	</#if>
</#if>
<#if infos??>
	<#if infos?size gt 0 >
	<#assign first = infos[0] />
		<@alert color='info' title='#i18n{portal.util.labelWarning}' iconTitle='info-circle' dismissible=true id='messages_infos_div'>
			<@unstyledList>
			 <#if first?is_string>
				<#list infos as info ><@li>${info!' #i18n{portal.util.labelWarning} '}</@li></#list>
	        <#else>
				<#list infos as info ><#if info.message??><@li>${info.message!' #i18n{portal.util.labelWarning} '}</@li></#if></#list>
	        </#if>
			</@unstyledList>
		</@alert>
	</#if>
</#if>
</#macro>