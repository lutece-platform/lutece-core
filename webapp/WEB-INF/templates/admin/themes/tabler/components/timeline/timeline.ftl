<#-- 
Macro: timeline

Description: Generates an HTML <ul> element to display a timeline, with an optional class and ID.

Parameters:
- class (string, optional): additional classes to add to the HTML code.
- id (string, optional): the ID of the <ul> element containing the timeline.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro timeline class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<ul class="list list-timeline<#if class !=''> ${class}</#if>"<#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
<#nested>
</ul>
</#macro>