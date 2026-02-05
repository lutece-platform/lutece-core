<#--
Macro: pageHeader

Description: Generates a header section for a page with an optional title and description. Includes a container for additional tools.

Parameters:
- id (string, optional): the ID of the header element.
- title (string, optional): the title of the page header.
- description (string, optional): the description of the page header.
-->
<#macro pageHeader id='' title='' titleClass='' description='' class='col-md-4 col-lg-5' toolsClass='' responsiveHeader=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div id="page-header" class="row g-2 justify-content-between align-items-center<#if responsiveHeader>flex-column flex-md-row</#if><#if titleClass !=''> ${titleClass!}</#if>">
	<div class="col<#if class !=''> ${class}</#if>">
		<div class="page-pretitle"></div>
		<h2 class="page-title mt-2">${title}</h2>
		<#if description !=''><p>${description}</p></#if>
	</div>
	<!-- Page title actions -->
	<div id="page-header-tools" class="col d-flex justify-content-end align-items-center d-print-none<#if toolsClass !=''> ${toolsClass}</#if>">
	<#nested>
	</div>
</div>
</#macro>