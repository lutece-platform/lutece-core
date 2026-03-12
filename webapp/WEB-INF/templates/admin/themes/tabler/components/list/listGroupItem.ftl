<#-- Macro: listGroupItem

Description: Generates an HTML list item (<li>) with the Bootstrap "list-group-item" and "list-group-item-action" classes, and optional attributes.

Parameters:
- id (string, optional): the ID attribute of the list item.
- class (string, optional): the class attribute of the list item.
- params (string, optional): additional attributes to add to the list item, in the form of a string of HTML attributes.

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
<#macro listGroupItem id='' class='' active=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li class="list-group-item list-group-item-action<#if class!=''> ${class}</#if><#if active> active</#if>"<#if id!=''> id="${id}"</#if><#if active> aria-current="true"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</li>
</#macro>