<#--
Macro: cPicture

Description: Generates an HTML picture element for responsive image rendering. Wraps source and img elements to serve different images based on media queries.

Parameters:
- id (string, optional): Unique identifier for the picture element. Default: ''.
- class (string, optional): CSS class(es) applied to the picture element. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro cPicture id='' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<picture<@commonAttrs id=id class=class params=params />>
<#nested/>
</picture>
</#macro>