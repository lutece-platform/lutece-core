<#-- 
Macro: tr

Description: Generates an HTML <tr> element with an optional ID, class, and various other features.

Parameters:
- id (string, optional): the ID of the <tr> element.
- class (string, optional): the class of the <tr> element.
- hide (string[], optional): an array of breakpoint names at which to hide the table row.
- align (string, optional): the horizontal alignment of the table cell contents.
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Basic table row:

    <@tr>
        <@td>John Doe</@td>
        <@td>john.doe@example.com</@td>
    </@tr>

    Table row hidden on small screens:

    <@tr hide=['xs', 'sm'] class='text-muted'>
        <@td>Additional details</@td>
        <@td>Only visible on medium screens and above</@td>
    </@tr>

-->
<#macro tr id='' class='' hide=[] params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local class += ' ' + displaySettings(hide,'table-cell') + ' ' + alignmentSettings(align) />
<tr<#if id!=''> id="${id}"</#if><#if class?trim!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</tr>
</#macro>