<#-- 
Macro: tableHead

Description: Generates an HTML <thead> element with an optional ID, class, and additional parameters.

Parameters:
- id (string, optional): the ID of the <thead> element.
- class (string, optional): the class of the <thead> element.
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Basic table header:

    <@tableHead>
        <@tr>
            <@th>#i18n{portal.users.columnName}</@th>
            <@th>#i18n{portal.users.columnEmail}</@th>
            <@th>#i18n{portal.users.columnStatus}</@th>
        </@tr>
    </@tableHead>

    Table header with custom class:

    <@tableHead id='header_users' class='bg-light'>
        <@tr>
            <@th>ID</@th>
            <@th>Label</@th>
        </@tr>
    </@tableHead>

-->
<#macro tableHead id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<thead<#if id!=''> id="${id}"</#if><#if class!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</thead>
</#macro>