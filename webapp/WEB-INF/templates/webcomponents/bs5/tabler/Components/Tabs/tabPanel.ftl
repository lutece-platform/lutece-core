<#-- 
Macro: tabPanel

Description: Generates an HTML <div> element for a tab panel, with an optional ID, parameters, and various other features.

Parameters:
- id (string, required): the ID of the <div> element for the tab panel.
- params (string, optional): additional parameters to add to the HTML code.
- active (boolean, optional): whether or not the tab panel is active.

-->
<#macro tabPanel id params='' active=false>
<div class="tab-pane mt-2 fade<#if active> show active</#if>" role="tabpanel" id="${id}" aria-labelledby="${id}-tab"<#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>