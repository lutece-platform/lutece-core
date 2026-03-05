<#-- 
Macro: tableFoot

Description: Generates an HTML <tfoot> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <tfoot> element.
- class (string, optional): the class of the <tfoot> element.
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Table footer with summary row:

    <@tableFoot>
        <@tr>
            <@td>Total</@td>
            <@td colspan=2>${totalCount}</@td>
        </@tr>
    </@tableFoot>

    Table footer with custom class:

    <@tableFoot class='bg-light fw-bold'>
        <@tr>
            <@td colspan=3>#i18n{portal.table.totalRecords}: ${items?size}</@td>
        </@tr>
    </@tableFoot>

-->
<#macro tableFoot id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<tfoot <#if id!=''> id="${id}"</#if><#if class!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</tfoot>
</#macro>