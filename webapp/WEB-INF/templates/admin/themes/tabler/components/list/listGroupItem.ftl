<#-- Macro: listGroupItem

Description: Generates an HTML list item (<li>) with the Bootstrap "list-group-item" and "list-group-item-action" classes, and optional attributes.

Parameters:
- id (string, optional): the ID attribute of the list item.
- class (string, optional): the class attribute of the list item.
- params (string, optional): additional attributes to add to the list item, in the form of a string of HTML attributes.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Simple list group item:

    <@listGroupItem>
        User account settings
    </@listGroupItem>

    Active list group item with custom class:

    <@listGroupItem active=true class='d-flex justify-content-between align-items-center'>
        Inbox <span class='badge bg-primary rounded-pill'>14</span>
    </@listGroupItem>

-->
<#macro listGroupItem id='' class='' active=false params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<li class="list-group-item list-group-item-action<#if class!=''> ${class}</#if><#if active> active</#if>"<@commonAttrs id=id /><#if active> aria-current="true"</#if><@commonAttrs params=params attrs=attrs />>
	<#nested>
</li>
</#macro>