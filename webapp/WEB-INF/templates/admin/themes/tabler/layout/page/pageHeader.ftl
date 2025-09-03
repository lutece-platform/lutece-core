<#--
Macro: pageHeader

Description: Generates a header section for a page with an optional title and description. Includes a container for additional tools.

Parameters:
- id (string, optional): the ID of the header element.
- title (string, optional): the title of the page header.
- description (string, optional): the description of the page header.
-->
<#macro pageHeader id='' title='' description='' class='' toolsClass='' responsiveHeader=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div id="page-header" class="row g-2 align-items-center<#if responsiveHeader>flex-column flex-md-row</#if> d-none">
	<div class="col<#if class !=''> ${class}</#if>">
		<h2 class="page-title">${title}</h2>
		<#if description !=''><p class="">${description}</p></#if>
	</div>
	<!-- Page title actions -->
	<div id="page-header-tools" class="col-auto ms-auto d-print-none<#if toolsClass !=''> ${toolsClass}</#if>">
	<#nested>
	</div>
</div>
<script>
// Initialize the feature title container
const adminContentHeader = document.querySelector('#admin-content-header');
const adminContentButtons = adminContentHeader.querySelector('.page-header-buttons');
const adminHeaderTitle = adminContentHeader.querySelector('.page-title');
const pageHeader = document.getElementById('page-header');
const pageHeaderTitle = pageHeader.querySelector('.page-title');
const pageHeaderTools = document.getElementById('page-header-tools');
if( adminHeaderTitle.textContent.trim() === pageHeaderTitle.textContent.trim() ) {
	pageHeader.remove();
} 

if ( pageHeaderTools != null && pageHeaderTools.textContent.trim() !== ''  ) {
	adminContentButtons.innerHTML = pageHeaderTools.innerHTML;
} else {
	pageHeader.classList.remove('d-none');
}
	
</script>
</#macro>