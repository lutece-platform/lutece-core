<#-- 
Macro: tabPanel

Description: Generates an HTML <div> element for a tab panel, with an optional ID, parameters, and various other features.

Parameters:
- id (string, required): the ID of the <div> element for the tab panel.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.
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
<#macro tabPanel id class='' params={} active=false deprecated...>
<@deprecatedWarning args=deprecated />
<div class="tab-pane fade<#if active> show active</#if><#if class?has_content> ${class}</#if>" role="tabpanel" id="${id}" aria-labelledby="${id}-tab"<@commonAttrs params=params />>
<#nested>
</div>
</#macro>