<#-- Macro: adminDashboardPanel
Description: Generates a collapsible panel for use in the admin dashboard. It generates a Bootstrap panel that includes a header with a title and an icon, and a body that can be expanded or collapsed.
Parameters:
- title (string, optional): the title to display in the panel header.
- parentId (string, optional): the ID of the parent element for the panel.
- childId (string, optional): the ID of the panel body.
- icon (string, optional): the icon to display in the panel header.
- color (string, optional): the color of the panel header, using a Bootstrap color class (e.g. "primary", "success", "danger").

Snippet:

    Basic dashboard panel with title and icon:

    <@adminDashboardPanel title='User Statistics' navTitle='Users' parentId='dashboard-tabs' childId='panel-users' icon='ti ti-users' color='primary'>
        <p>Total users: 150</p>
    </@adminDashboardPanel>

    Dashboard panel with a different color:

    <@adminDashboardPanel title='System Status' navTitle='System' parentId='dashboard-tabs' childId='panel-system' icon='ti ti-server' color='success'>
        <p>All systems operational.</p>
    </@adminDashboardPanel>

-->
<#macro adminDashboardPanel title='' navTitle='' parentId='' childId='' icon='' color='' deprecated...>
<@deprecatedWarning args=deprecated />
<#assign parentId=parentId />
<@tabPanel id='${childId}' params='title="${title}" data-nav="${navTitle}" data-icon="${icon}" data-color="${color}"'>
<@pageHeader title="${title}" />
<#nested>
</@tabPanel>
</#macro>