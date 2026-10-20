<#--
Macro: cBlock

Description: Generates a generic HTML block-level container element (div or section). Useful for wrapping content sections on skin pages.

Parameters:
- type (string, optional): HTML tag to use for the block ('div', 'section', etc.). Default: 'div'.
- class (string, optional): CSS class(es) applied to the block element. Default: ''.
- id (string, optional): Unique identifier for the block element. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro cBlock type='div' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<${type}<@commonAttrs id=id class=class params=params />>
<#nested>
</${type}>
</#macro>