<#-- 
Macro: progressBar

Description: Generates a progress bar with an optional description and ID.

Parameters:
- description (string, optional): a description of the progress bar.
- id (string, optional): the ID of the progress bar.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro progressBar description='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="progress"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<div id="progressbar" class="progress-bar progress-bar-striped" role="progressbar">
		<div id="complexity">0%</div>
	</div>
</div>
<#if description!=''>
	<span class="progress-description">${description}</span>
</#if>
</#macro>