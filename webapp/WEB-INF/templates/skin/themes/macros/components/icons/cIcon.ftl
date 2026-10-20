<#--
Macro: cIcon

Description: Generates an icon element using a CSS icon font (Tabler Icons by default).

Parameters:
- name (string, optional): Name of the icon. Default: 'check'.
- class (string, optional): Additional CSS classes for the icon. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.
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

    <@cIcon name='alert-circle' class='text-danger' params={'aria-hidden': 'true'} />

-->
<#macro cIcon name='check' class='' id='' title='' params={} prefix='ti ti-' type='span' deprecated...>
<@deprecatedWarning args=deprecated />
<#local iconParams = params + title?has_content?then( { 'title': title, 'role': 'img', 'aria-label': title }, { 'aria-hidden': 'true' } ) />
<@cInline type=type class='${prefix}${name} ${class}' id=id params=iconParams />
</#macro>