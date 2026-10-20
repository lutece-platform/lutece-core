<#--
Macro: cSection

Description: Generates a semantic HTML section container using a configurable block-level tag. Structures page content into logical sections on skin pages.

Parameters:
- type (string, required): HTML tag used for the section element ('section', 'div', 'aside', etc.). Default: 'section'.
- class (string, optional): CSS class(es) applied to the section element. Default: ''.
- id (string, optional): Unique identifier for the section element. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Section - @cSection
- newFeature: false

Snippet:

    Basic usage:

    <@cSection>
        <p>Welcome to our services section.</p>
    </@cSection>

    As a div with custom class:

    <@cSection type='div' class='container py-4' id='services'>
        <p>Our services are listed below.</p>
    </@cSection>

-->
<#macro cSection type='section' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<${type}<@commonAttrs id=id class=class params=params />>
<#nested>
</${type}>
</#macro>