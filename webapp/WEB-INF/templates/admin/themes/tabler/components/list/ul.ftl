<#-- Macro: ul

Description: Generates an HTML unordered list (<ul>) with optional attributes and classes, including support for alignment, display settings, collapsing, and additional parameters.
Note: This macro depends on the "alignmentSettings" and "displaySettings" functions.

Parameters:
- id (string, optional): the ID attribute of the unordered list.
- class (string, optional): the class attribute of the unordered list.
- align (string, optional): the alignment of the unordered list within its parent element ("left", "center", or "right").
- hide (list, optional): a list of strings representing the CSS display values for hiding the unordered list at different screen sizes (e.g. ["md:hidden"] to hide the unordered list on medium screens and up).
- collapsed (boolean, optional): whether the unordered list should be initially collapsed (hidden).
- params (string, optional): additional attributes to add to the unordered list, in the form of a string of HTML attributes.

Snippet:

    Basic unordered list:

    <@ul>
        <@li>First item</@li>
        <@li>Second item</@li>
        <@li>Third item</@li>
    </@ul>

    Centered list with custom class:

    <@ul class='list-unstyled' align='center' id='feature-list'>
        <@li>Feature A</@li>
        <@li>Feature B</@li>
    </@ul>

    Collapsed list (initially hidden):

    <@ul id='collapsible-list' collapsed=true>
        <@li>Hidden item 1</@li>
        <@li>Hidden item 2</@li>
    </@ul>

-->
<#macro ul id='' class='' align='' hide=[] collapsed=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local class += ' ' + alignmentSettings(align,'') + ' ' + displaySettings(hide,'block') />
<#if collapsed><#local class += ' ' + 'collapse' /></#if>
<ul<#if class?trim!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if><#if id!=''> id="${id}"</#if>>
<#nested>
</ul>
</#macro>