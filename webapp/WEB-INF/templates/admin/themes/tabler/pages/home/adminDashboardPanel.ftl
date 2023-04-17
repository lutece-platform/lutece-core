<#-- Macro: adminDashboardPanel
Description: Generates a collapsible panel for use in the admin dashboard. It generates a Bootstrap panel that includes a header with a title and an icon, and a body that can be expanded or collapsed.
Parameters:
- title (string, optional): the title to display in the panel header.
- parentId (string, optional): the ID of the parent element for the panel.
- childId (string, optional): the ID of the panel body.
- icon (string, optional): the icon to display in the panel header.
- color (string, optional): the color of the panel header, using a Bootstrap color class (e.g. "primary", "success", "danger").
-->
<#macro adminDashboardPanel title='' parentId='' childId='' icon='' color=''>
<#assign parentId=parentId />
<@tabPanel id='${childId}' params="title='${title}' ">
<@pageHeader title="${title}" />
<#nested>
</@tabPanel>

<script>
document.addEventListener( "DOMContentLoaded", function(){
	const urlHash = document.location.hash, idHash = document.querySelector( urlHash ) ;
	if ( urlHash != undefined) {
		idHash.classList.add('show')
	}
});
</script>
</#macro>