<#-- 
Macro: tableFoot

Description: Generates an HTML <tfoot> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <tfoot> element.
- class (string, optional): the class of the <tfoot> element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro tableFoot id='' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<tfoot <@commonAttrs id=id class=class params=params />>
<#nested>
</tfoot>
</#macro>