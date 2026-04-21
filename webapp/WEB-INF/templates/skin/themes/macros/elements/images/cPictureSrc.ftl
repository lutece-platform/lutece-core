<#--
Macro: cPictureSrc

Description: Generates an HTML source element for use inside a picture element. Defines an image source with optional media query and MIME type for responsive image delivery.

Parameters:
- srcset (string, required): Source URL or set of image sources.
- media (string, optional): Media query condition for when this source applies. Default: ''.
- type (string, optional): MIME type of the image resource (e.g., 'image/webp'). Default: ''.
- class (string, optional): CSS class(es) applied to the source element. Default: ''.
- id (string, optional): Unique identifier for the source element. Default: ''.
- params (string, optional): Additional HTML attributes for the source element. Default: ''.

Snippet:

    Basic usage inside a cPicture:

    <@cPicture>
        <@cPictureSrc srcset='images/hero-desktop.webp' media='(min-width: 1024px)' type='image/webp' />
        <@cPictureSrc srcset='images/hero-mobile.jpg' />
        <@cImg src='images/hero-fallback.jpg' alt='Hero image' />
    </@cPicture>

-->
<#macro cPictureSrc srcset media='' type='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<source srcset="${srcset}" <#if media!=''>media="${media!}"</#if><#if type!=''> type="${type!}"</#if><#if class!=''> class="${class!}"</#if><#if id!=''> id="${id!}"</#if><#if params!=''> ${params!}</#if>>
</#macro>