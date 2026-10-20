<#-- 
Macro: tabContent

Description: Generates a tab content container with an optional ID and parameters.

Parameters:
- id (string, optional): the ID of the tab content container.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic tab content container:

    <@tabContent>
        <@tabPanel id='panel1' active=true>First panel content</@tabPanel>
        <@tabPanel id='panel2'>Second panel content</@tabPanel>
    </@tabContent>

    Tab content with custom ID:

    <@tabContent id='myTabContent' class='p-3'>
        <@tabPanel id='settings' active=true>Settings panel</@tabPanel>
    </@tabContent>

-->
<#macro tabContent class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card-body<#if class?has_content> ${class}</#if>">
<div class="tab-content"<@commonAttrs id=id params=params />>
<#nested>
</div>
</div>
</#macro>