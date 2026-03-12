<#--
Macro: row

Description: Generates a row container element for a Bootstrap grid system. Includes options for additional CSS classes, ID, and collapse functionality.

Parameters:
- class (string, optional): the CSS class of the row container element.
- id (string, optional): the ID of the row container element.
- collapsed (boolean, optional): whether the row should be initially collapsed.
- align (string, optional): the alignment of the row container element (left, right, or center).
- params (string, optional): additional parameters to be added to the row container element.

Snippet:

    Basic row with columns:

    <@row>
        <@columns md=6>Left column</@columns>
        <@columns md=6>Right column</@columns>
    </@row>

    Row with alignment, custom ID, and gap:

    <@row id='actions-row' class='g-3' align='center'>
        <@columns md=4>Centered content</@columns>
    </@row>

    Collapsible row:

    <@row id='details-row' collapsed=true>
        <@columns xs=12>
            <p>This content is initially hidden and can be toggled</p>
        </@columns>
    </@row>

-->
<#macro row class='' id='' collapsed=false align='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<#if align!=''><#local class += ' ' + alignmentSettings(align,'') /></#if>
<div class="row<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>