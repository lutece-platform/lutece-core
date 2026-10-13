<#--
Macro: cTitle

Description: Generates an HTML heading element (h1-h6) with a configurable level. Used to display titles and headings on skin pages.

Parameters:
- level (number, optional): Heading level from 1 to 6. Default: 1.
- id (string, optional): Unique identifier for the heading element. Default: ''.
- class (string, optional): CSS class(es) applied to the heading element. Default: ''.
- params (string, optional): Additional HTML attributes for the heading element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Showcase:
- desc: Titre - @cTitle
- newFeature: false

Snippet:

    Basic usage:

    <@cTitle level=1>Page Title</@cTitle>

    With custom class:

    <@cTitle level=2 class='section-title text-primary' id='services-heading'>
        Our Services
    </@cTitle>

    Subheading:

    <@cTitle level=3>Contact Information</@cTitle>

-->
<#macro cTitle level=1 id='' class='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<h${level}<@commonAttrs id=id class=class params=params attrs=attrs />>
<#nested/>
</h${level}>
</#macro>