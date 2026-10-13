<#--
Macro: cFooter

Description: Generates an HTML footer element with an optional title. Suitable for page footers or section footers on skin pages.

Parameters:
- title (string, optional): Title displayed inside the footer. Default: ''.
- titleLevel (number, optional): Heading level for the footer title (1-6). Default: 2.
- class (string, optional): CSS class(es) applied to the footer element. Default: ''.
- id (string, optional): Unique identifier for the footer element. Default: ''.
- params (string, optional): Additional HTML attributes for the footer element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Showcase:
- desc: Pied de page - @cFooter
- newFeature: false

Snippet:

    Basic usage:

    <@cFooter>
        <p>Copyright 2026 - All rights reserved.</p>
    </@cFooter>

    With a title:

    <@cFooter title='Site Information' titleLevel=3 class='site-footer'>
        <p>Contact us at info@example.com</p>
    </@cFooter>

-->
<#macro cFooter title='' titleLevel=2 class='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<footer<@commonAttrs id=id class=class params=params attrs=attrs /> >
    <#if title?has_content><@cTitle level=titleLevel>${title}</@cTitle></#if>
    <#nested>
</footer>
</#macro>