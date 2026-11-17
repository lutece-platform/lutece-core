<#--
Macro: cStat

Description: Generates a key figure: a value, a label and an optional description and icon. The value and the label are read together by screen readers.

Parameters:
- value (string, required): The figure, formatted for display (e.g. '170 000', '45 %'). Default: ''.
- label (string, required): What the figure counts. Default: ''.
- description (string, optional): Additional information (period, source). Default: ''.
- icon (string, optional): Name of an icon (cIcon) displayed above the figure. Default: ''.
- plain (boolean, optional): No background. Default: false.
- listItem (boolean, optional): Wraps the stat in a li, to use inside cStatGroup. Default: false.
- class (string, optional): Additional CSS classes. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Chiffre clé - @cStat
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Single key figure:

    <@cStat value='45 %' label='des déchets recyclés' description='Source : Ville de Paris, 2025' icon='refresh' />

-->
<#macro cStat value='' label='' description='' icon='' plain=false listItem=false class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#if listItem><li></#if>
<div class="lutece-ds-stat<#if plain> lutece-ds-stat-plain</#if><#if class!=''> ${class}</#if>"<@commonAttrs id=id params=params />>
<#if icon!=''><span class="lutece-ds-stat-icon"><@cIcon name=icon /></span></#if>
<p class="lutece-ds-stat-value">${value}<span class="visually-hidden"> ${label}</span></p>
<p class="lutece-ds-stat-label" aria-hidden="true">${label}</p>
<#if description!=''><p class="lutece-ds-stat-desc">${description}</p></#if>
<#nested>
</div>
<#if listItem></li></#if>
</#macro>
