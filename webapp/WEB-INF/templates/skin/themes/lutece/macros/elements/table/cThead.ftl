<#--
Macro: cThead

Description: Generates an HTML thead element to define the header section of a table. Used inside a cTable to wrap header rows containing cTh cells.

Parameters:
- class (string, optional): CSS class(es) applied to the thead element. Default: ''.
- id (string, optional): Unique identifier for the thead element. Default: ''.
- params (string, optional): Additional HTML attributes for the thead element. Default: ''.

Snippet:

    Basic usage inside a cTable:

    <@cTable caption='Appointments' id='appointments-table'>
        <@cThead>
            <@cTr>
                <@cTh>Date</@cTh>
                <@cTh>Time</@cTh>
                <@cTh>Service</@cTh>
            </@cTr>
        </@cThead>
        <@cTbody>
            <@cTr>
                <@cTd>2026-03-10</@cTd>
                <@cTd>14:00</@cTd>
                <@cTd>Civil Status</@cTd>
            </@cTr>
        </@cTbody>
    </@cTable>

-->  
<#macro cThead class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<thead <#if class!=''>thead-${class!}</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params!}</#if> >
<#nested>
</thead>
</#macro>