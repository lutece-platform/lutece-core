<#-- 
Macro: tableHeadBodySeparator

Parameters:
- id (string, optional): the ID of the <tbody> element.
- class (string, optional): the class of the <tbody> element.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro tableHeadBodySeparator id='' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
</thead>
<tbody<@commonAttrs id=id class=class params=params />>
</#macro>