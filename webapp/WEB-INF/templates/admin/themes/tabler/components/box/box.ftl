<#--
Macro: box
Description: Generates HTML box elements for displaying content.
Parameters:
- color (string, optional): the background color of the box element.
- id (string, optional): the ID of the box element.
- style (string, optional): the text color of the box element.
- class (string, optional): the CSS class of the box element.
- title (string, optional): the title of the box element.
- collapsed (boolean, optional): whether the box should be collapsed by default.
- params (string, optional): additional HTML attributes to include in the box element.
-->
<#macro box color='' id='' style='' class='' title='' collapsed=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card mb-3<#if color!=''> card-${color}<#else> card-transparent</#if> card-outline<#if style!=''> text-${style}</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#if title =''>
<#nested>
<#else>
<@boxHeader title=title collapsed=isCollapsed params=params skipHeader=true />
<@boxBody collapsed = collapsed>
<#nested>
</@boxBody>
</#if>
</div>
</#macro>