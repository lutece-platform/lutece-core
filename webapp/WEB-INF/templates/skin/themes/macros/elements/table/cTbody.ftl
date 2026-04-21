<#--
Macro: cTbody

Description: Generates an HTML tbody element to group the body rows of a table. Used inside a cTable to wrap data rows.

Parameters:
- class (string, optional): CSS class(es) applied to the tbody element. Default: ''.
- id (string, optional): Unique identifier for the tbody element. Default: ''.
- params (string, optional): Additional HTML attributes for the tbody element. Default: ''.

Snippet:

    Basic usage inside a cTable:

    <@cTable caption='Users' id='users-table'>
        <@cThead>
            <@cTr><@cTh>Name</@cTh><@cTh>Email</@cTh></@cTr>
        </@cThead>
        <@cTbody>
            <@cTr><@cTd>John Doe</@cTd><@cTd>john@example.com</@cTd></@cTr>
            <@cTr><@cTd>Jane Smith</@cTd><@cTd>jane@example.com</@cTd></@cTr>
        </@cTbody>
    </@cTable>

-->
<#macro cTbody class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<tbody <#if class!=''>${class!}</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params!}</#if> >
<#nested>
</tbody>
</#macro>