<#-- Macro: adminDashboardWidget
Description: Generates a widget for use in the admin dashboard. It generates a Bootstrap card that includes a header with a title and control buttons, and a body that can display any content.
Parameters:
- id (string, required): the ID of the widget.
- title (string, required): the title to display in the widget header.
- color (string, optional): the color of the widget header, using a Bootstrap color class (e.g. "primary", "success", "danger").
- url (string, optional): the URL of a settings page for the widget.
- class (string, optional): the class to apply to the widget element.
- bodyClass (string, optional): the class to apply to the widget body element.
- params (string, optional): additional parameters to add to the widget element.

Snippet:

    Basic dashboard widget with title and content:

    <@adminDashboardWidget id='widget-users' title='Active Users' color='primary'>
        <p>There are currently <strong>42</strong> active users.</p>
    </@adminDashboardWidget>

    Dashboard widget with a settings URL and custom body class:

    <@adminDashboardWidget id='widget-stats' title='Statistics' color='success' url='jsp/admin/plugins/stats/ManageStats.jsp' bodyClass='p-0'>
        <table class="table table-striped mb-0">
            <tr><td>Visits</td><td>1,234</td></tr>
            <tr><td>Page Views</td><td>5,678</td></tr>
        </table>
    </@adminDashboardWidget>

    Dashboard widget without header:

    <@adminDashboardWidget id='widget-quick' title='Quick Actions' hasHeader=false>
        <@aButton href='jsp/admin/ManageUsers.jsp' color='primary' title='Manage Users' />
    </@adminDashboardWidget>

-->
<#macro adminDashboardWidget id title hasHeader=true sm=12 md=4 color='primary' url='' class='' bodyClass='' actions=true actionMenu='' params='' deprecated...>
<@deprecatedWarning args=deprecated />

<@box style='solid' color='${color}' id='${id}_dashboard_card' class='box-widget' params=' data-id="${id}" draggable="true"'>
<#if hasHeader>
<@boxHeader titleLevel='h3' title=title! titleActions=actions>
<div class="dropdown">
	<a href="#" class="btn-action" data-bs-toggle="dropdown">
	<!-- Download SVG icon from http://tabler.io/icons/icon/dots-vertical -->
	<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-1">
		<path d="M12 12m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0"></path>
		<path d="M12 19m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0"></path>
		<path d="M12 5m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0"></path>
	</svg>
	</a>
	<div class="dropdown-menu dropdown-menu-end">
		<#if actionMenu!=''>${actionMenu!}</#if>
		<#if url!=''><@aButton color='link' class='dropdown-item' href=url! title='#i18n{portal.util.labelShow} ${title!}' /></#if>
		<@button color='link' class='dropdown-item text-danger' style='card-control remove' buttonTargetId='#${id}_dashboard_card' title='#i18n{portal.util.labelHide}' />
	</div>
</div>
</@boxHeader>
</#if>
<@boxBody class=bodyClass id='${id}_dashboard_card_body'>
<#if !hasHeader><h3 class="card-title">${title}</h3></#if>
<@row>
<@columns>
<#nested>
</@columns>
<#if !hasHeader && actionMenu !=''>
<@columns md=1>
<div class="dropdown ">
	<a href="#" class="btn-action" data-bs-toggle="dropdown">
	<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-1">
		<path d="M12 12m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0"></path>
		<path d="M12 19m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0"></path>
		<path d="M12 5m-1 0a1 1 0 1 0 2 0a1 1 0 1 0 -2 0"></path>
	</svg>
	</a>
	<div class="dropdown-menu dropdown-menu-end">
		${actionMenu!}
	</div>
</div>
</@columns>
</#if>
</@row>
</@boxBody>
</@box>
</#macro>