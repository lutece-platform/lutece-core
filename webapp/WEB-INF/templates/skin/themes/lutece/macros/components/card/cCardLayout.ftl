<#--
Macro: cCardLayout

Description: Generates a grid layout container for organizing multiple cards in group, deck, or masonry column modes.

Parameters:
- type (string, optional): Layout type ('group', 'deck', 'columns'). Default: 'group'.
- rowCols (string, optional): Number of columns per row for deck layout. Default: ''.
- class (string, optional): Additional CSS class(es) for the grid container. Default: ''.
- id (string, optional): The unique identifier for the card layout. Default: ''.
- params (string, optional): Additional HTML attributes for the card layout. Default: ''.

Snippet:

    Card deck with 3 columns:

    <@cCardLayout type='deck' rowCols='3'>
        <@cCard title='Service 1'>
            <p>Description of service 1.</p>
        </@cCard>
        <@cCard title='Service 2'>
            <p>Description of service 2.</p>
        </@cCard>
        <@cCard title='Service 3'>
            <p>Description of service 3.</p>
        </@cCard>
    </@cCardLayout>

-->
<#macro cCardLayout type='group' rowCols='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if type == 'group'>
    <#local cClass>row g-0</#local>
<#elseif type == 'deck'>
    <#local cClass>row g-4 row-cols-${rowCols!} ${class!}</#local>
<#elseif type == 'columns'>
    <#local cParams>data-masonry='{"percentPosition": true }' ${params!}</#local>
    <#local cClass>row ${class!}</#local>
</#if>
<@cBlock class=cClass id=id params=cParams>
<#nested>
</@cBlock>
<#if type == 'columns'>
    <script src="${commonsSiteThemePath}${commonsSiteJsPath}vendor/masonry/masonry.pkgd.min.js" charset="utf-8"></script>
</#if>
</#macro>