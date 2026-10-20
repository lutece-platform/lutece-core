<#--
Macro: cFigure

Description: Generates an HTML figure element with an optional caption. Wraps images or illustrations with a semantic container on skin pages.

Parameters:
- caption (string, optional): Caption text displayed below the figure via a figcaption element. Default: ''.
- id (string, optional): Unique identifier for the figure element. Default: ''.
- class (string, optional): CSS class(es) applied to the figure element. Default: 'figure'.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: Figure - @cFigure
- bs: content/figures
- newFeature: false

Snippet:

    Basic usage:

    <@cFigure caption='City hall at sunset'>
        <@cImg src='images/city-hall.jpg' alt='City hall' />
    </@cFigure>

    Without caption:

    <@cFigure class='figure text-center'>
        <@cImg src='images/banner.jpg' alt='Welcome banner' />
    </@cFigure>

-->
<#macro cFigure caption='' id='' class='figure' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<figure <@commonAttrs id=id class=class params=params />>
<#nested/>
<#if caption?has_content><@cFigCaption class='figure-caption'>${caption!}</@cFigCaption></#if>
</figure>
</#macro>