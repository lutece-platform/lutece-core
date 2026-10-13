<#--
Macro: cIcon

Description: Generates an icon element using a CSS icon font (Tabler Icons by default).

Parameters:
- name (string, optional): Name of the icon. Default: 'check'.
- class (string, optional): Additional CSS classes for the icon. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
- prefix (string, optional): CSS class prefix for the icon font. Default: 'ti ti-'.
- type (string, optional): HTML element type to render. Default: 'span'.

Showcase:
- desc: Icône - @cIcon
- newFeature: false

Snippet:

    Basic usage:

    <@cIcon name='home' />

    With custom prefix and class:

    <@cIcon name='user' prefix='bi bi-' class='fs-4 text-primary' />

    As an inline element with extra attributes:

    <@cIcon name='alert-circle' class='text-danger' params='aria-hidden="true"' />

-->
<#macro cIcon name='check' class='' id='' title='' params='' attrs={} prefix='ti ti-' type='span' deprecated...>
<@deprecatedWarning args=deprecated />
<#local iconParams><#if title?has_content>${params} title="${title}"<#else>${params}</#if></#local>
<@cInline type=type class='${prefix}${name} ${class}' id=id params=iconParams attrs=attrs />
</#macro>