<#-- 
Macro: tabContent

Description: Generates a tab content container with an optional ID and parameters.

Parameters:
- id (string, optional): the ID of the tab content container.
- params (string, optional): additional parameters to add to the HTML code.

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
<#macro tabContent class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card-body<#if class!=''> ${class}</#if>">
<div class="tab-content"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</div>
</#macro>