<#-- Macro: adminSiteColumnOutline
Description: Wraps a column's content in an outline that includes the column's ID. It generates a Bootstrap column outline with the given column ID.
Parameters:
- columnid (string, optional): the ID of the column to be displayed in the outline.
-->
<#macro adminSiteColumnOutline columnid=''>
<@div class="admin_column_outline">
	<span class="admin_column_id">${i18n("portal.site.columnId",columnid)}</span>
	<@div><#nested></@div>
</@div>
</#macro>