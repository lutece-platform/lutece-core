<#--
Macro: cPicture

Description: Generates an HTML picture element for responsive image rendering. Wraps source and img elements to serve different images based on media queries.

Parameters:
- id (string, optional): Unique identifier for the picture element. Default: ''.
- class (string, optional): CSS class(es) applied to the picture element. Default: ''.
- params (string, optional): Additional HTML attributes for the picture element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Showcase:
- desc: Picture responsive - @cPicture
- newFeature: false

Snippet:

    Basic usage:

    <@cPicture>
        <@cPictureSrc srcset='images/banner-large.webp' media='(min-width: 768px)' type='image/webp' />
        <@cPictureSrc srcset='images/banner-small.webp' type='image/webp' />
        <@cImg src='images/banner.jpg' alt='Welcome banner' />
    </@cPicture>

-->
<#macro cPicture id='' class='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<picture<@commonAttrs id=id class=class params=params attrs=attrs />>
<#nested/>
</picture>
</#macro>