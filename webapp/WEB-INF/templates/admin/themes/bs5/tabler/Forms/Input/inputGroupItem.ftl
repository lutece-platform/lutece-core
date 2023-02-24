<#--
Macro: inputGroupItem

Description: Generates an input group item, which can be used to add buttons, text, or other form elements to an input group.

Parameters:
- id (string, optional): the ID of the input group item.
- pos (string, optional): the position of the input group item relative to the input field. Can be 'append' or 'prepend'. Default is 'append'.
- type (string, optional): the type of the input group item. Can be 'btn' or 'text'. Default is 'btn'.
- params (string, optional): additional parameters for the input group item.
-->
<#macro inputGroupItem id='' pos='append' type='text' params=''>
<#-- pos: append / prepend | Default append -->
<#-- type: btn/text. default is btn	-->
<span class="input-group-${type}"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</span>
</#macro>