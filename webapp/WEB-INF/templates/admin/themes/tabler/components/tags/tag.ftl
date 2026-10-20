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
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.
- deprecated (string, optional): an optional message indicating that this macro is deprecated.

Snippet:

    Basic primary tag:

    <@tag>Published</@tag>

    Tag with color and icon:

    <@tag color='success' tagIcon='check'>Approved</@tag>

    Tag with custom title and warning color:

    <@tag color='warning' title='Pending review' tagIcon='clock'>Pending</@tag>

-->
<#macro tag color='primary' class='' size='' title='' tagIcon='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />	
<span class="badge<#if color?has_content> bg-${color} text-bg-${color}</#if><#if class?has_content> ${class}</#if>"<#if title?has_content> title='${title}'</#if><#if id?has_content>id='${id}'</#if><@commonAttrs params=params />>
	<#if tagIcon?has_content>
	<@icon style=tagIcon />
	</#if>
	<#nested>
</span>
</#macro>