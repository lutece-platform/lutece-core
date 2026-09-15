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

Snippet:

    Simple stat widget with icon and color:

    <@smallBox title='150' text='Total Users' boxIcon='users' color='primary' />

    Stat widget with unit and link:

    <@smallBox title='85' unit='%' text='Server Uptime' boxIcon='server' color='success' url='jsp/admin/system/ServerStatus.jsp' />

    Stat widget with custom ID:

    <@smallBox title='42' text='Pending Tasks' boxIcon='clipboard' color='warning' id='pending-tasks-widget' />

-->
<#macro smallBox color='' title='' text='' boxIcon='' titleLevel='div' unit='' url='' urlText='' id='' params='' fontSize='40px' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card card-sm mb-3 box-widget" draggable='true' <#if id?has_content> id="${id}" data-id="${id}"</#if><#if params?has_content> ${params}</#if>>
	<div class="card-body">
		<div class="row align-items-center">
			<div class="col-auto">
				<#if color?has_content><span class="bg-${color} text-white avatar"></#if>
					<@icon style=boxIcon  />
				<#if color?has_content></span></#if>
			</div>
			<div class="col">
			<#if url?has_content><a class="card-link" href="${url}"></#if>
			<${titleLevel} class="font-weight-medium"><span class="counter">${title}</span><#if unit?has_content> ${unit}</#if></${titleLevel}>
			<div class="text-muted">${text}</div>
			<#if url?has_content></a></#if>
			</div>
		</div>
	</div>
</div>
</#macro>