<#--
Macro: cBtnImg

Description: Generates a button with an embedded image or SVG, and a text label from nested content.

Parameters:
- src (string, required): The image source URL or SVG markup for the button.
- class (string, optional): CSS class(es) for the button. Default: 'primary'.
- id (string, optional): The unique identifier for the button. Default: ''.
- type (string, optional): The button type ('submit' or 'svg' for inline SVG). Default: 'submit'.
- params (string, optional): Additional HTML attributes for the button. Default: ''.
- imgPos (string, optional): Position of the image relative to the label ('before', 'after'). Default: 'before'.
- disabled (boolean, optional): If true, disables the button. Default: false.

Snippet:

    Button with image before label:

    <@cBtnImg src='images/icon-upload.png' class='primary'>
        Upload file
    </@cBtnImg>

    Button with image after label:

    <@cBtnImg src='images/icon-next.png' class='secondary' imgPos='after'>
        Next step
    </@cBtnImg>

-->
<#macro cBtnImg src class='primary' id='' type='submit' params='' imgPos='before' disabled=false deprecated...>
<@deprecatedWarning args=deprecated />
<@cBtn label='' class='${class!} btn-img' type=submit  disabled=disabled params=params >
<#if imgPos='before'>
    <#if type='svg'>
    <@cFigure>${src}</@cFigure>
    <#else>
    <@cImg src="${src!}" alt='' />
    </#if>
</#if>
<@cText type='span'><#nested></@cText>
<#if imgPos='after'>
    <#if type='svg'>
        <@cFigure>${src}</@cFigure>
    <#else>
        <@cImg src="${src!}" title="${title!}" alt='' class='after' />
    </#if>
</#if>
</@cBtn>
</#macro>