<#-- Macro: tform

Description: Generates a Bootstrap form element with specified properties.

Parameters:
- type (string, optional): the type of form (horizontal, inline, or blank).
- class (string, optional): additional CSS classes to add to the form element.
- align (string, optional): the alignment of the form element (left, right, or center).
- hide (list, optional): a list of form properties to hide.
- action (string, optional): the URL to submit the form to.
- method (string, optional): the HTTP method to use when submitting the form (get or post).
- name (string, optional): the name of the form element.
- id (string, optional): the ID of the form element.
- role (string, optional): the role of the form element.
- collapsed (boolean, optional): whether to collapse the form element.
- enctype (string, optional): the encoding type for the form element.
- params (string, optional): additional parameters to add to the form element.

-->
<#macro tform type='' class='' align='' hide=[] action='' method='post' name='' id='' role='' collapsed=false enctype='' boxed=false params='' deprecated...>
<@deprecatedWarning args=deprecated />	
<#local class = class />
<#if align!=''><#local class += ' ' + alignmentSettings(align,'') /></#if>
<#if hide??><#local class += ' ' + displaySettings(hide,'block') /></#if>
<#if collapsed><#local class += ' collapse' /></#if>
<#switch type>
	<#case 'horizontal'>
		<#local class += ''>
		<#break>
	<#case 'inline'>
		<#local class += ' d-inline-flex'>
		<#break>
	<#default>
		<#local class += ''>
</#switch>
<#if boxed><div class="card"><div class="card-body"></#if>
<form <#if class!=''>class="${class?trim} <#if align='middle'>align-middle</#if>"</#if><#if id!=''> id="${id}"</#if><#if action!=''> action="${action}"</#if><#if method!=''> method="${method}"</#if><#if name!=''> name="${name}"</#if><#if role!=''> role="${role}"</#if><#if method='post' && enctype!=''> enctype='${enctype}'</#if><#if params!=''> ${params}</#if>>
	<#nested>
</form>
<#if boxed></div></div></#if>

</#macro>