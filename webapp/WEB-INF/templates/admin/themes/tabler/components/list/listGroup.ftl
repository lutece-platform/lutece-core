<#-- Macro: listGroup

Description: Generates an HTML unordered list (<ul>) with the Bootstrap "list-group" class and optional attributes.

Parameters:
- id (string, optional): the ID attribute of the unordered list.
- class (string, optional): the class attribute of the unordered list.
- params (string, optional): additional attributes to add to the unordered list, in the form of a string of HTML attributes.

Snippet:

    Basic list group:

    <@listGroup>
        <@listGroupItem>First item</@listGroupItem>
        <@listGroupItem>Second item</@listGroupItem>
        <@listGroupItem>Third item</@listGroupItem>
    </@listGroup>

    List group with custom class and ID:

    <@listGroup id='task-list' class='list-group-flush'>
        <@listGroupItem active=true>Current task</@listGroupItem>
        <@listGroupItem>Next task</@listGroupItem>
    </@listGroup>

-->
<#macro listGroup id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="list-group<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</ul>
</#macro>