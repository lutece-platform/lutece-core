<#--
Macro: span

Description: Generates an HTML span element with a specified ID, class, display settings, alignment, and nested content block.

Parameters:
- id (string, optional): the ID for the span element.
- class (string, optional): additional classes to add to the span element.
- hide (list, optional): a list of display settings to hide the span element, e.g., ["md", "lg"].
- collapsed (boolean, optional): whether to collapse the span element.
- align (string, optional): the alignment setting for the span element, e.g., "center" or "right".
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Default h2 heading:

    <@h>
        Page Title
    </@h>

    H1 heading with a custom class and ID:

    <@h level=1 class='mb-4' id='main-title'>
        Dashboard
    </@h>

    H3 heading with center alignment:

    <@h level=3 align='center'>
        Section Subtitle
    </@h>

-->
<#macro h level=2 id='' class='' hide=[] align='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local class += ' ' + alignmentSettings( align,'' ) + ' ' + displaySettings( hide, 'inline-flex' ) />
<h${level}<#if class?trim!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if><#if id!=''> id="${id}"</#if>>
<#nested>
</h${level}>
</#macro>