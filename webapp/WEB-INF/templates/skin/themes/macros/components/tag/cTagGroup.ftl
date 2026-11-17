<#--
Macro: cTagGroup

Description: Generates a list of tags (cTag), for example the active filters of a search.

Parameters:
- label (string, optional): Accessible name of the list, read by screen readers (aria-label). Default: ''.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Groupe d'étiquettes - @cTagGroup
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Active filters:

    <@cTagGroup label='Filtres actifs'>
        <@cTag label='Paris 11e' removeUrl='jsp/site/Portal.jsp?page=search' listItem=true />
        <@cTag label='Sport' removeUrl='jsp/site/Portal.jsp?page=search' listItem=true />
    </@cTagGroup>

-->
<#macro cTagGroup label='' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="lutece-ds-tag-group<#if class!=''> ${class}</#if>"<#if label!=''> aria-label="${label}"</#if><@commonAttrs id=id params=params />>
<#nested>
</ul>
</#macro>
