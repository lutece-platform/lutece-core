<#--
Macro: cCardLayout

Description: Generates a grid layout container for organizing multiple cards in group, deck, or masonry column modes.

Parameters:
- type (string, optional): Layout type ('group', 'deck', 'columns'). Default: 'group'.
- rowCols (string, optional): Number of columns per row for deck layout. Default: ''.
- class (string, optional): Additional CSS class(es) for the grid container. Default: ''.
- id (string, optional): The unique identifier for the card layout. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Layout carte - @cCardLayout
- bs: components/card
- newFeature: false
- updatedFeature: false
- deprecated: false

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
<#macro cCardLayout type='group' rowCols='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local cParams = params />
<#if type == 'group'>
    <#local cClass = 'row g-0' />
<#elseif type == 'deck'>
    <#local cClass>row g-4 row-cols-${rowCols!} ${class!}</#local>
<#elseif type == 'columns'>
    <#local cParams = params + { 'data-masonry': '{"percentPosition": true }' } />
    <#local cClass>row ${class!}</#local>
</#if>
<@cBlock class=cClass id=id params=cParams>
<#nested>
</@cBlock>
<#if type == 'columns'>
    <script src="${commonsSharedThemePath}${commonsSiteJsPath}vendor/masonry/masonry.pkgd.min.js" charset="utf-8"></script>
</#if>
</#macro>