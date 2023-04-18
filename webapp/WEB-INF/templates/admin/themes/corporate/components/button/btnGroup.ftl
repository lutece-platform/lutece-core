<#-- Macro: btnGroup

Description: Generates a button group container with customizable attributes.

Parameters:
- id (string, optional): the ID attribute of the container.
- class (string, optional): additional CSS classes to add to the container.
- align (string, optional): the alignment of the container's content.
- size (string, optional): the size of the container.
- params (string, optional): additional parameters to add to the container.
- ariaLabel (string, optional): the ARIA label of the container.
- hide (array, optional): an array of breakpoints to hide the container.
- deprecated (string, optional): a message indicating that the macro is deprecated.
-->
<#macro btnGroup id='' class='' align='' size='' params='' ariaLabel='' hide=[]   deprecated...>
<@deprecatedWarning args=deprecated />	
<#local class += ' ' + displaySettings(hide,'inline-flex') + ' ' + alignmentSettings(align,'') />
<div class="btn-group<#if size!=''> btn-group-${size}</#if><#if class!=''> ${class?trim}</#if>" role="group"<#if ariaLabel!=''> aria-label="${ariaLabel}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<#nested>
</div>
</#macro>