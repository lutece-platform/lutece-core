<#--
Macro: Figure

Description: Generates an HTML figure element with an optionnal figcaption.

Parameters:
- caption (string, optional): Add the caption.
- captionPos (string, optional): Default value bottom. If the value is top the ficgaption is above.
- class (string, optional): additional classes to add to the image element.
- id (string, optional): the ID for the image element.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro figure caption='' captionPos='bottom' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<figure<#if class!=''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> />
<#if captionPos='top'><#if caption!=''><figcaption>${caption}</figcaption></#if></#if>
<#nested>
</figure>
<#if captionPos='bottom'><#if caption!=''><figcaption>${caption}</figcaption></#if></#if>
</#macro>