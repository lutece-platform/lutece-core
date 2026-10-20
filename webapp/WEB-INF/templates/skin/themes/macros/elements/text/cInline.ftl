<#--
Macro: cInline

Description: Generates a generic HTML inline element (span by default). Useful for wrapping inline content with custom styling or attributes on skin pages.

Parameters:
- type (string, optional): HTML tag to use for the inline element ('span', 'em', 'strong', etc.). Default: 'span'.
- class (string, optional): CSS class(es) applied to the inline element. Default: ''.
- id (string, optional): Unique identifier for the inline element. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Span inline - @cInline
- newFeature: false

Snippet:

    Basic usage:

    <@cInline class='text-primary'>Highlighted text</@cInline>

    As a strong element:

    <@cInline type='strong' class='fw-bold'>Important notice</@cInline>

    With custom attributes:

    <@cInline type='em' params={'data-toggle': 'tooltip', 'title': 'Additional info'}>Hover for details</@cInline>

-->
<#macro cInline type='span' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<${type}<@commonAttrs id=id class=class params=params />>
<#nested>
</${type}>
</#macro>