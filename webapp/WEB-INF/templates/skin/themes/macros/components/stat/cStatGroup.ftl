<#--
Macro: cStatGroup

Description: Generates a responsive grid of key figures (cStat): 3 columns by default, 2 on tablets, 1 on phones.

Parameters:
- label (string, optional): Accessible name of the list, read by screen readers (aria-label). Default: ''.
- cols (number, optional): Columns on large screens: 2, 3 or 4. Default: 3.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Chiffres clés - @cStatGroup
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Key figures:

    <@cStatGroup label='Chiffres clés' cols=3>
        <@cStat value='170 000' label='arbres plantés' description='depuis 2020' listItem=true />
        <@cStat value='1 000 km' label='de pistes cyclables' listItem=true />
        <@cStat value='300' label='rues aux écoles' listItem=true />
    </@cStatGroup>

-->
<#macro cStatGroup label='' cols=3 class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="lutece-ds-stat-group<#if cols!=3> lutece-ds-stat-group-${cols}</#if><#if class!=''> ${class}</#if>"<#if label!=''> aria-label="${label}"</#if><@commonAttrs id=id params=params />>
<#nested>
</ul>
</#macro>
