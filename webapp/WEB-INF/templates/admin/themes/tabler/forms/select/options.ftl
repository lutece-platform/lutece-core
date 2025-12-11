<#--
Macro: options

Description: Generates a list of option elements for use in a select list.

Parameters:
- items (list): a list of items to be displayed as options. Each item should have a 'name' and a 'code'.
- selected (string, optional): the code of the option that should be selected by default. If not provided, the 'selected' property of each item will be used.
- id (boolean, optional): whether to add an ID to each option element. Default is false.
- class (string, optional): the CSS class of each option element.
- params (string, optional): additional parameters for each option element.
-->

<#macro options items selected='' id=false class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if items??>
	<#list items as item>
		<#local idItem><#if id>${item.code}_${item?index}<#else></#if></#local>
		<#local selectedItem><#if selected !=''><#if item.code?string=selected>true<#else>false</#if><#else>${item.selected?c}</#if></#local>
		<@option label=item.name value=item.code id=idItem class=class selected=selectedItem?boolean disabled=item.disabled params=params /> 
	</#list>
</#if>
</#macro>