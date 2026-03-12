<#-- 
Macro: tableHeadBodySeparator

Parameters:
- id (string, optional): the ID of the <tbody> element.
- class (string, optional): the class of the <tbody> element.
- params (string, optional): additional parameters to add to the HTML code.

Snippet:

    Separator between thead and tbody inside a headBody table:

    <@table headBody=true>
        <@tr>
            <@th>Name</@th>
            <@th>Email</@th>
        </@tr>
        <@tableHeadBodySeparator />
        <@tr>
            <@td>John Doe</@td>
            <@td>john.doe@example.com</@td>
        </@tr>
    </@table>

    Separator with an ID on the tbody:

    <@table headBody=true>
        <@tr>
            <@th>Product</@th>
            <@th>Price</@th>
        </@tr>
        <@tableHeadBodySeparator id='product_body' />
        <@tr>
            <@td>Widget</@td>
            <@td>9.99</@td>
        </@tr>
    </@table>

-->
<#macro tableHeadBodySeparator id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
</thead>
<tbody<#if id!=''> id="${id}"</#if><#if class!=''> class="${class?trim}"</#if><#if params!=''> ${params}</#if>>
</#macro>