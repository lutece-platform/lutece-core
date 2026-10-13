<#-- 
Macro: tableFoot

Description: Generates an HTML <tfoot> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <tfoot> element.
- class (string, optional): the class of the <tfoot> element.
- params (string, optional): additional parameters to add to the HTML code.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

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
<#macro tableFoot id='' class='' params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<tfoot <@commonAttrs id=id /><#if class!=''> class="${class?trim}"</#if><@commonAttrs params=params attrs=attrs />>
<#nested>
</tfoot>
</#macro>