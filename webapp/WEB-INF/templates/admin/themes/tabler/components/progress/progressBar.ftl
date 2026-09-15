<#-- 
Macro: progressBar

Description: Generates a progress bar with an optional description and ID.

Parameters:
- description (string, optional): a description of the progress bar.
- id (string, optional): the ID of the progress bar.
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Basic progress bar:

    <@progressBar />

    Progress bar with description:

    <@progressBar description='Password strength' id='passwordStrength' />

-->
<#macro progressBar description='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="progress"<#if id?has_content> id="${id}"</#if><#if params?has_content> ${params}</#if>>
	<div id="progressbar" class="progress-bar progress-bar-striped" role="progressbar">
		<div id="complexity">0%</div>
	</div>
</div>
<#if description?has_content>
	<span class="progress-description">${description}</span>
</#if>
</#macro>