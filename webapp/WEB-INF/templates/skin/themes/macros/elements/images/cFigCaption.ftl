<#--
Macro: cFigCaption

Description: Generates an HTML figcaption element for use inside a figure. Provides a text caption describing an image or illustration.

Parameters:
- id (string, optional): Unique identifier for the figcaption element. Default: ''.
- class (string, optional): CSS class(es) applied to the figcaption element. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic usage:

    <@cFigCaption>
        Photo of the city hall during the annual celebration.
    </@cFigCaption>

    With a custom class:

    <@cFigCaption class='figure-caption text-muted'>
        Source: City archives, 2025.
    </@cFigCaption>

-->
<#macro cFigCaption id='' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<figcaption <@commonAttrs id=id class=class params=params />>
<#nested/>
</figcaption>
</#macro>