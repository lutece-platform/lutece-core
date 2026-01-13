<#--
Macro: smallBox
Description: Generates an HTML element for a small box widget with an icon, title, and text.
Parameters:
- color (string, optional): the background color of the widget.
- title (string, required): the title of the widget.
- text (string, required): the text content of the widget.
- boxIcon (string, optional): the icon to use in the widget.
- titleLevel (string, optional): the HTML heading level for the title of the widget.
- unit (string, optional): a unit to display next to the title.
- url (string, optional): a URL to link the widget to.
- urlText (string, optional): the text to display for the URL link.
- id (string, optional): the ID of the widget element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the widget element.
- fontSize (string, optional): the font size of the widget icon.
-->
<#macro smallBox color='' title='' text='' boxIcon='' titleLevel='div' unit='' url='' urlText='' id='' params='' fontSize='40px' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card card-sm mb-3 box-widget" draggable='true' <#if id!=''> id="${id}" data-id="${id}"</#if><#if params!=''> ${params}</#if>>
	<div class="card-body">
		<div class="row align-items-center">
			<div class="col-auto">
				<#if color !=''><span class="bg-${color} text-white avatar"></#if>
					<@icon style=boxIcon  />
				<#if color !=''></span></#if>
			</div>
			<div class="col">
			<#if url!=''><a class="card-link" href="${url}"></#if>
			<${titleLevel} class="font-weight-medium"><span class="counter">${title}</span><#if unit!=''> ${unit}</#if></${titleLevel}>
			<div class="text-muted">${text}</div>
			<#if url!=''></a></#if>
			</div>
		</div>
	</div>
</div>
</#macro>