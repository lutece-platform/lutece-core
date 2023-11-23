<#-- Macro: card

Description: Generates a Bootstrap card container with customizable attributes.

Parameters:
- class : Default "md-3"
- id: default ''
- header (boolean, optional): whether to include a header in the card.
- headerTitle (string, optional): the title of the card header.
- headerIcon (boolean, optional): whether to include an icon in the card header.
- headerTitleIcon (string, optional): the icon to display in the card header title.
- status : Color for status default ""
- status_pos : Position for status default "start"
- ribbon : Text of a primary color ribbon on top (default)
- ribbon_color: Ribbon's color default "primary"
- ribbon_pos: Ribbon's position - default "top"
- stamp: SVG code for a background image set on right/top of the card
- stamp_color: Stamp's color default "primary"
- params : Any other attribute
- #nested : Conrenu du body
-->
<#macro card class='mb-3' id='' headerTitle='' headerClass='bg-info' headerIcon='' headerActions='' status='' statusPos='start' ribbon='' ribbonColor='primary' ribbonPos='top' stamp='' stampColor='primary' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card ${class}"<#if id !=''> id="${id}"</#if>>
	<#if stamp !=''><div class="card-stamp"><div class="card-stamp-icon bg-${stampColor}">${stamp}</div></div></#if>
	<#if status !=''><div class="card-status-start bg-${status}"></div></#if>
	<#if ribbon !=''><div class="ribbon ribbon-${ribbonPos} bg-${ribbonColor}">${ribbon}</div></#if>
	<#if headerTitle !='' || headerActions !=''>
	<div class="card-header ${headerClass}">
		<#if headerIcon!=''><@icon style='${headerIcon}' />&#160;</#if>${headerTitle}
		<#if headerActions !=''>
		<div class="card-actions">${headerActions}</div>
		</#if>
	</div>
	</#if>
	<div class="card-body">
	<#nested>
	</div>
</div>
</#macro>