<#-- 
Macro: tabContent

Description: Generates a tab content container with an optional ID and parameters.

Parameters:
- id (string, optional): the ID of the tab content container.
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

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
<#macro tabContent class='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card-body<#if class?has_content> ${class}</#if>">
<div class="tab-content"<@commonAttrs id=id params=params attrs=attrs />>
<#nested>
</div>
</div>
</#macro>