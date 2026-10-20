<#--
Macro: Figure

Description: Generates an HTML figure element with an optionnal figcaption.

Parameters:
- caption (string, optional): Add the caption.
- captionPos (string, optional): Default value bottom. If the value is top the ficgaption is above.
- class (string, optional): additional classes to add to the image element.
- id (string, optional): the ID for the image element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Figure with an image and a bottom caption:

    <@figure caption='An example image with caption'>
        <@img url='images/photo.jpg' alt='Example image' />
    </@figure>

    Figure with a top caption and custom class:

    <@figure caption='Chart: Monthly revenue' captionPos='top' class='text-center'>
        <@img url='images/chart.png' alt='Revenue chart' />
    </@figure>

-->
<#macro figure caption='' captionPos='bottom' class='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<figure<@commonAttrs id=id class=class params=params /> />
<#if captionPos='top'><#if caption?has_content><figcaption>${caption}</figcaption></#if></#if>
<#nested>
</figure>
<#if captionPos='bottom'><#if caption?has_content><figcaption>${caption}</figcaption></#if></#if>
</#macro>