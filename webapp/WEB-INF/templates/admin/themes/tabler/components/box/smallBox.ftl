<#--
Macro: smallBox
Description: Generates an HTML element for a small box widget with an icon, title, and text.
Parameters:
- color (string, optional): the color of the square behind the icon (a Tabler color: primary, green, azure...).
- title (string, required): the title of the widget.
- text (string, required): the text content of the widget.
- boxIcon (string, optional): the icon to use in the widget.
- titleLevel (string, optional): the HTML heading level for the title of the widget.
- unit (string, optional): a unit to display next to the title.
- url (string, optional): a URL to link the widget to.
- urlText (string, optional): the text to display for the URL link.
- id (string, optional): the ID of the widget element. If not provided, a default ID will be generated.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.
- fontSize (string, optional): the font size of the widget icon.
- progress (number, optional): a percentage, shown as a thin bar under the figure.

Snippet:

    Simple stat widget with icon and color:

    <@smallBox title='150' text='Total Users' boxIcon='users' color='primary' />

    Stat widget with unit and link:

    <@smallBox title='85' unit='%' text='Server Uptime' boxIcon='server' color='success' url='jsp/admin/system/ServerStatus.jsp' />

    Stat widget with custom ID:

    <@smallBox title='42' text='Pending Tasks' boxIcon='clipboard' color='warning' id='pending-tasks-widget' />

-->
<#macro smallBox color='' title='' text='' boxIcon='' titleLevel='div' unit='' url='' urlText='' id='' params={} fontSize='40px' progress='' deprecated...>
<@deprecatedWarning args=deprecated />
<#-- A figure, as the stat cards of the Tabler dashboards: the label on one line with the icon on its right, the figure under it, the whole card a link to its feature -->
<div class="card card-sm box-widget lutece-kpi" draggable='true' <#if id?has_content> id="${id}" data-id="${id}" data-dw-title="${text}"</#if><@commonAttrs params=params />>
	<div class="card-body">
		<#-- The button that hides the figure, shown in place of its icon when the card is pointed or focused (needs an id, to keep the choice) -->
		<#if id?has_content><button type="button" class="btn-action lutece-kpi-hide card-control remove" data-bs-dismiss="alert" data-bs-target="#${id}" title="#i18n{portal.util.labelHide} ${text}"><i class="ti ti-eye-off" aria-hidden="true"></i><span class="visually-hidden">#i18n{portal.util.labelHide} ${text}</span></button></#if>
		<div class="d-flex align-items-center gap-2 mb-1">
			<div class="subheader text-truncate" title="${text}">${text}</div>
			<#if boxIcon?has_content><span class="avatar avatar-square ms-auto<#if color?has_content> bg-${color}-lt</#if>"><@icon style=boxIcon /></span></#if>
		</div>
		<${titleLevel} class="lutece-kpi-value"><span class="counter">${title}</span><#if unit?has_content> <span class="fs-5 fw-normal text-secondary">${unit}</span></#if></${titleLevel}>
		<#if progress?has_content>
		<div class="progress progress-sm mt-1" role="progressbar" aria-valuenow="${progress}" aria-valuemin="0" aria-valuemax="100"><div class="progress-bar<#if color?has_content> bg-${color}</#if>" style="width: ${progress}%"></div></div>
		</#if>
		<#if url?has_content>
		<a class="stretched-link" href="${url}" title="<#if urlText?has_content>${urlText}<#else>${text}</#if>"><span class="visually-hidden"><#if urlText?has_content>${urlText}<#else>${text}</#if></span></a>
		</#if>
	</div>
</div>
</#macro>