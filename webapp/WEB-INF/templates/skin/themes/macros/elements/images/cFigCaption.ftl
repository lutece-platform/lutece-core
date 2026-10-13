<#--
Macro: cFigCaption

Description: Generates an HTML figcaption element for use inside a figure. Provides a text caption describing an image or illustration.

Parameters:
- id (string, optional): Unique identifier for the figcaption element. Default: ''.
- class (string, optional): CSS class(es) applied to the figcaption element. Default: ''.
- params (string, optional): Additional HTML attributes for the figcaption element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

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
<#macro cFigCaption id='' class='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<figcaption <@commonAttrs id=id class=class params=params attrs=attrs />>
<#nested/>
</figcaption>
</#macro>