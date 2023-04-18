<#--
Macro: inputGroupItem

Description: Generates an input group item, which can be used to add buttons, text, or other form elements to an input group.

Parameters:
- id (string, optional): the ID of the input group item.
- pos (string, optional): the position of the input group item relative to the input field. Can be 'append' or 'prepend'. Default is 'append'.
- type (string, optional): the type of the input group item. Can be 'btn', 'icon' or 'text'. Default is 'text'.
- params (string, optional): additional parameters for the input group item.
-->
<#macro inputGroupItem id='' class='' pos='append' type='text'  params='' deprecated...>
<@deprecatedWarning args=deprecated />
<span class="<#if type='icon'>input-icon-addon<#else>input-group-${type}</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</span>
</#macro>