<#-- Macro: listGroup

Description: Generates an HTML unordered list (<ul>) with the Bootstrap "list-group" class and optional attributes.

Parameters:
- id (string, optional): the ID attribute of the unordered list.
- class (string, optional): the class attribute of the unordered list.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro listGroup id='' class='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="list-group<#if class?has_content> ${class}</#if>"<@commonAttrs id=id params=params />>
	<#nested>
</ul>
</#macro>