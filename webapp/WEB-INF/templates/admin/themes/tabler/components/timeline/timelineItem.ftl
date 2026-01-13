<#-- 
Macro: timelineItem

Description: Generates an item for a timeline list.

Parameters:
- iconFace (string, optional): the name of the icon face to use.
- iconBg (string, optional): the background color for the icon.
- time (string, optional): the time associated with the item.
- label (string, required): the label for the item.
- footer (string, optional): the footer text for the item.
- class (string, optional): additional classes to add to the list item.
- id (string, optional): the ID for the list item.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro timelineItem iconFace='check' iconBg='bg-primary' time='' label='' footer='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<li<#if class !=''> class="${class}"</#if><#if id !=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
	<#if iconFace !=''><div class="list-timeline-icon ${iconBg}"><@icon style=iconFace /></div></#if>
	<div class="list-timeline-content">
		<#if time !=''><div class="list-timeline-time">${time}</div></#if>
		<p class="list-timeline-title">${label}</p>
		<div class="list-timeline-body"><#nested></div>
		<#if footer !=''><div class="timeline-footer">${footer}</div></#if>
	</div>
</li>
</#macro>