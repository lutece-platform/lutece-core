<#-- Function: alignmentSettings

Description: Returns a string of CSS classes to align content based on the specified alignment and style.

Parameters:
- align (string, optional): the alignment of the content (left, right, or center).
- style (string, optional): the style of the content (text or blank).
-->
<#function alignmentSettings align='' style=''>
<#local x = ''>
<#if align !=''>
	<#if align = 'left'>
		<#if style = 'text'>
			<#local x = 'text-left' />
		<#elseif style = ''>
			<#local x = 'd-flex justify-content-start' />
		</#if>
	<#elseif align = 'right'>
		<#if style = 'text'>
			<#local x = 'text-right' />
		<#elseif style = ''>
			<#local x = 'd-flex justify-content-end' />
		</#if>
	<#elseif align = 'center'>
		<#if style = 'text'>
			<#local x = 'text-center' />
		<#elseif style = ''>
			<#local x = 'd-flex justify-content-center' />
		</#if>
	</#if>
</#if>
<#return x>
</#function>