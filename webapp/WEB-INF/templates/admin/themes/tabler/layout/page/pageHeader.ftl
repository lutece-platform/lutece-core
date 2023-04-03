<#--
Macro: pageHeader

Description: Generates a header section for a page with an optional title and description. Includes a container for additional tools.

Parameters:
- id (string, optional): the ID of the header element.
- title (string, optional): the title of the page header.
- description (string, optional): the description of the page header.
-->
<#macro pageHeader id='' title='' description=''>
<div id="pageHeader">
	<div class="d-lg-flex align-items-center justify-content-between">
	  <div class="flex-grow-1 py-2">
	  <h1 class="mb-0 fw-bolder">${title}</h1>
	  </div>
	    <div id="pageHeaderTools" class="py-2">
		<#nested>
	  </div>
	</div>
	<p class="">${description}</p>
</div>
</#macro>