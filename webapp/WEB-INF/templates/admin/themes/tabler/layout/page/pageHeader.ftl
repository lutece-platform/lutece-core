<#--
Macro: pageHeader

Description: Generates a header section for a page with an optional title and description. Includes a container for additional tools.

Parameters:
- id (string, optional): the ID of the header element.
- title (string, optional): the title of the page header.
- description (string, optional): the description of the page header.
- titleClass (string, optional): additional CSS classes for the header wrapper. Default is 'mb-1'.
- class (string, optional): CSS classes for the title column. Default is 'col-md-4 col-lg-5'.
- toolsClass (string, optional): additional CSS classes for the tools container.
- responsiveHeader (boolean, optional): if true, the header layout switches to column direction on small screens. Default is false.
- params (string, optional): additional HTML attributes for the header element.

Snippet:

    Simple page header with title:

    <@pageHeader title='User Management' />

    Page header with title, description, and action buttons:

    <@pageHeader title='Dashboard' description='Overview of recent activity'>
        <a class="btn btn-primary" href="#"><i class="ti ti-plus"></i> Add Widget</a>
    </@pageHeader>

    Responsive page header with custom classes:

    <@pageHeader title='Reports' description='Monthly analytics' responsiveHeader=true toolsClass='gap-2'>
        <a class="btn btn-outline-primary" href="#">Export</a>
        <a class="btn btn-primary" href="#">Generate</a>
    </@pageHeader>

-->
<#macro pageHeader id='' title='' titleClass='mb-1' description='' class='col-md-4 col-lg-5' toolsClass='' responsiveHeader=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div id="page-header" class="row g-2 justify-content-between align-items-center<#if responsiveHeader>flex-column flex-md-row</#if><#if titleClass !=''> ${titleClass!}</#if>">
	<div class="col-12<#if class !=''> ${class}</#if>">
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