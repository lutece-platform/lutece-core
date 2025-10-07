<#--
Macro: pageHeader

Description: Generates a header section for a page with an optional title and description. Includes a container for additional tools.

Parameters:
- id (string, optional): the ID of the header element.
- title (string, optional): the title of the page header.
- description (string, optional): the description of the page header.
-->
<#macro pageHeader id='' title='' titleClass='' description='' class='' toolsClass='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div id="pageHeader" class="mb-3 <#if toolsClass !=''>${class}</#if>">
<div class="d-lg-flex align-items-center justify-content-between">
	<div class="flex-grow-1 py-2">
		<h1 class="mb-0 fw-bolder ${titleClass!}">${title}</h1>
		<#if description !=''>
			<p class="lead">${description}</p>
		</#if>
	</div>
	<#local nestedContent><#nested></#local>
  	<#if nestedContent?has_content>
	<div id="pageHeaderTools" class="py-2<#if toolsClass !=''> ${toolsClass}</#if>">
		<#nested>
	</div>
	</#if>
</div>
</div>
</#macro>