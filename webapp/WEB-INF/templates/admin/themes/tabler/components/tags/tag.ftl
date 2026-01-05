<#-- 
Macro: tag

Description: Generates an HTML <span> element containing a badge or tag, with an optional color, size, title, and icon.

Parameters:
- color (string, optional): the color of the badge or tag.
- class (string, optional): additional classes to add to the HTML code.
- size (string, optional): the size of the badge or tag.
- title (string, optional): the title text of the badge or tag.
- tagIcon (string, optional): the icon to display alongside the badge or tag.
- id (string, optional): the ID of the <span> element containing the badge or tag.
- params (string, optional): additional parameters to add to the HTML code.
- deprecated (string, optional): an optional message indicating that this macro is deprecated.

-->
<#macro tag color='primary' class='' size='' title='' tagIcon='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />	
<span class="tag<#if color!=''> bg-${color} text-${color}-fg</#if><#if class!=''> ${class}</#if>"<#if title!=''> title='${title}'</#if><#if id!=''>id='${id}'</#if><#if params!=''>${params}</#if>>
	<#if tagIcon !=''>
	<@icon style=tagIcon />
	</#if>
	<#nested>
</span>
</#macro>