<#--
Macro: cTfoot

Description: Generates an HTML tfoot element to define the footer section of a table. Used inside a cTable for summary rows or totals.

Parameters:
- class (string, optional): CSS class(es) applied to the tfoot element. Default: ''.
- id (string, optional): Unique identifier for the tfoot element. Default: ''.
- params (string, optional): Additional HTML attributes for the tfoot element. Default: ''.

Snippet:

    Basic usage inside a cTable:

    <@cTable caption='Budget overview' id='budget-table'>
        <@cThead>
            <@cTr><@cTh>Category</@cTh><@cTh>Amount</@cTh></@cTr>
        </@cThead>
        <@cTbody>
            <@cTr><@cTd>Education</@cTd><@cTd>50 000</@cTd></@cTr>
            <@cTr><@cTd>Transport</@cTd><@cTd>30 000</@cTd></@cTr>
        </@cTbody>
        <@cTfoot>
            <@cTr><@cTd>Total</@cTd><@cTd>80 000</@cTd></@cTr>
        </@cTfoot>
    </@cTable>

--> 
<#macro cTfoot class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<tfoot <#if class!=''>thead-${class!}</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params!}</#if> >
<#nested>
</tfoot>
</#macro>