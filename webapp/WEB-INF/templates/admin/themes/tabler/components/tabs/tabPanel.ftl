<#-- 
Macro: tabPanel

Description: Generates an HTML <div> element for a tab panel, with an optional ID, parameters, and various other features.

Parameters:
- id (string, required): the ID of the <div> element for the tab panel.
- params (string, optional): additional parameters to add to the HTML code.
- active (boolean, optional): whether or not the tab panel is active.

Snippet:

    Active tab panel:

    <@tabPanel id='overview' active=true>
        <p>This panel is visible by default.</p>
    </@tabPanel>

    Hidden tab panel with custom class:

    <@tabPanel id='details' class='p-4'>
        <p>This panel is hidden until its tab is clicked.</p>
    </@tabPanel>

-->
<#macro tabPanel id class='' params='' active=false deprecated...>
<@deprecatedWarning args=deprecated />
<div class="tab-pane fade<#if active> show active</#if><#if class!=''> ${class}</#if>" role="tabpanel" id="${id}" aria-labelledby="${id}-tab"<#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>