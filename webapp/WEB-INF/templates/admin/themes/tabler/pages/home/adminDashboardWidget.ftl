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
- boxIcon (string, optional): a Tabler icon name shown on a light square before the title.
- iconColor (string, optional): the Tabler color of that square (azure, green, purple...); 'primary' when empty.
- subtitle (string, optional): a line of text under the title.
- headerActions (string, optional): HTML of buttons shown in the header, before the link and the menu (e.g. a "Create" button).
- flush (boolean, optional): the content goes against the edges of the card, without padding nor wrapper, for a list-group-flush or a table.

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

    Compact dashboard widget, its content a flush list, with an icon, a subtitle and a header button:

    <#assign createButton><a class="btn btn-sm btn-primary" href="jsp/admin/plugins/myplugin/CreateEntity.jsp"><i class="ti ti-plus" aria-hidden="true"></i>Create</a></#assign>
    <@adminDashboardWidget id='widget-entities' title='Entities' boxIcon='list' iconColor='azure' subtitle='3 entities' url='jsp/admin/plugins/myplugin/ManageEntities.jsp' headerActions=createButton flush=true>
        <div class="list-group list-group-flush">
            <div class="list-group-item">Entity 1</div>
        </div>
    </@adminDashboardWidget>

    Dashboard widget without header:

    <@adminDashboardWidget id='widget-quick' title='Quick Actions' hasHeader=false>
        <@aButton href='jsp/admin/ManageUsers.jsp' color='primary' title='Manage Users' />
    </@adminDashboardWidget>

-->
<#macro adminDashboardWidget id title hasHeader=true sm=12 md=4 color='primary' url='' class='' bodyClass='vmax-dvh-50 overflow-y-auto ' boxContentFooter='' actions=true actionMenu='' params='' boxIcon='' iconColor='' subtitle='' headerActions='' flush=false deprecated...>
<@deprecatedWarning args=deprecated />
<#--
	The header of the Tabler "card actions": the icon, the title and its subtitle on the left, the actions on the right: the buttons of
	the widget, a link to its feature and the menu, which keeps the entries of the widget and the one that hides it
-->
<@box style='solid' color='${color}' id='${id}_dashboard_card' class='box-widget${class?has_content?then(" " + class, "")}' params={"data-id": (id), "draggable": "true"}>
<#if hasHeader>
<div class="card-header">
	<#if boxIcon?has_content><span class="avatar avatar-sm bg-${iconColor?has_content?then(iconColor, 'primary')}-lt"><@icon style=boxIcon /></span></#if>
	<div class="min-w-0">
		<h3 class="card-title text-truncate">${title}</h3>
		<#if subtitle?has_content><p class="card-subtitle text-truncate">${subtitle}</p></#if>
	</div>
	<#if actions>
	<div class="card-actions btn-actions">
		<#if headerActions?has_content>${headerActions}</#if>
		<#if url?has_content><a href="${url}" class="btn-action" title="#i18n{portal.util.labelShow} ${title}"><i class="ti ti-arrow-up-right" aria-hidden="true"></i><span class="visually-hidden">#i18n{portal.util.labelShow} ${title}</span></a></#if>
		<div class="dropdown">
			<a href="#" class="btn-action" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" title="#i18n{portal.util.labelActions}"><i class="ti ti-dots-vertical" aria-hidden="true"></i><span class="visually-hidden">#i18n{portal.util.labelActions}</span></a>
			<div class="dropdown-menu dropdown-menu-end">
				<#if actionMenu?has_content>${actionMenu}</#if>
				<@button color='link' class='dropdown-item text-danger' style='card-control remove' buttonTargetId='#${id}_dashboard_card' title='#i18n{portal.util.labelHide}' />
			</div>
		</div>
	</div>
	</#if>
</div>
</#if>
<#if flush>
<div class="card-body p-0 ${bodyClass}" id="${id}_dashboard_card_body">
<#if !hasHeader><h3 class="card-title px-3 pt-3">${title}</h3></#if>
<#nested>
</div>
<#else>
<@boxBody class='${bodyClass}' id='${id}_dashboard_card_body'>
<#if !hasHeader><h3 class="card-title">${title}</h3></#if>
<@row>
<@columns>
<#nested>
</@columns>
<#if !hasHeader && actionMenu?has_content>
<@columns md=1>
<div class="dropdown">
	<a href="#" class="btn-action" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false" title="#i18n{portal.util.labelActions}"><i class="ti ti-dots-vertical" aria-hidden="true"></i><span class="visually-hidden">#i18n{portal.util.labelActions}</span></a>
	<div class="dropdown-menu dropdown-menu-end">
		${actionMenu!}
	</div>
</div>
</@columns>
</#if>
</@row>
</@boxBody>
</#if>
<#if boxContentFooter?has_content>
<@boxFooter>
${boxContentFooter!}
</@boxFooter>
</#if>
</@box>
</#macro>