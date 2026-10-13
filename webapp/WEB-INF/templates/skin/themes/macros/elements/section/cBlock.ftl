<#--
Macro: cBlock

Description: Generates a generic HTML block-level container element (div or section). Useful for wrapping content sections on skin pages.

Parameters:
- type (string, optional): HTML tag to use for the block ('div', 'section', etc.). Default: 'div'.
- class (string, optional): CSS class(es) applied to the block element. Default: ''.
- id (string, optional): Unique identifier for the block element. Default: ''.
- params (string, optional): Additional HTML attributes for the block element. Default: ''.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Showcase:
- desc: Bloc div - @cBlock
- newFeature: false

Snippet:

    Basic usage:

    <@cBlock>
        <p>Content inside a div block.</p>
    </@cBlock>

    As a section with classes:

    <@cBlock type='section' class='content-wrapper' id='main-content'>
        <p>Main page content goes here.</p>
    </@cBlock>

-->
<#macro cBlock type='div' class='' id='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<${type}<@commonAttrs id=id class=class params=params attrs=attrs />>
<#nested>
</${type}>
</#macro>