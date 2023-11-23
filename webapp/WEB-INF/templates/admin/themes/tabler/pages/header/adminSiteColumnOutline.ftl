<#-- Macro: adminSiteColumnOutline
Description: Wraps a column's content in an outline that includes the column's ID. It generates a Bootstrap column outline with the given column ID.
Parameters:
- columnid (string, optional): the ID of the column to be displayed in the outline.
-->
<#macro adminSiteColumnOutline columnid=''>
<@div id='lutece-column-${columnid}' class='lutece-admin-column' params='data-no-content="#i18n{portal.site.portletType.labelCreateColumn}"'>
    <@div class='lutece-column-toolbar'>
		<@tag class='lutece-admin-column-id' color='dark'>${i18n("portal.site.columnId",columnid)}</@tag>
		<@button color='primary btn-column-add-portlet' params='data-bs-toggle="modal" data-portlet-column="${columnid}" data-portlet-order=""' title='#i18n{portal.site.portletType.labelCreateColumn}' hideTitle=['all'] buttonIcon='layout-grid-add' />
	</@div>
    <@div class="lutece-admin-column-outline"><#compress><#nested></#compress></@div>
</@div>
</#macro>