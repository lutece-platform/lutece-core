<#-- 
Macro: progressBar

Description: Generates a progress bar with an optional description and ID.

Parameters:
- description (string, optional): a description of the progress bar.
- id (string, optional): the ID of the progress bar.
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic progress bar:

    <@progressBar />

    Progress bar with description:

    <@progressBar description='Password strength' id='passwordStrength' />

-->
<#macro progressBar description='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<div class="progress"<@commonAttrs id=id params=params attrs=attrs />>
	<div id="progressbar" class="progress-bar progress-bar-striped" role="progressbar">
		<div id="complexity">0%</div>
	</div>
</div>
<#if description!=''>
	<span class="progress-description">${description}</span>
</#if>
</#macro>