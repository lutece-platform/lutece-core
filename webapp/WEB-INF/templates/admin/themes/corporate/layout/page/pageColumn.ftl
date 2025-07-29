<#--
Macro: pageColumn
Description: Generates a container element for a full-height column on a page with an optional fixed width and custom CSS classes. The container includes a nested container for page content.
Parameters:
- id (string, optional): The ID of the container element.
- width (string, optional): The width of the container element. Default is 100%.
- class (string, optional): The CSS class of the container element. Use "p-" prefix to add padding classes, e.g. "p-2" or "p-5".
- height (string, optional): The height of the container element. Use "full" to set the height to 100% of the viewport height.
- title (string, optional): The title to be displayed at the top of the container element. If provided, the container will have a fixed height and a scrollable content area.
- flush (boolean, optional): If true, padding will not be added to the container. Default is false.
- center (boolean, optional): If true, content inside the container will be vertically and horizontally centered. Default is false.
- responsiveMenuSize (string, optional): If provided, the container will be hidden when the viewport width is less than the value. Accepts standard breakpoints (xs, sm, md, lg, xl, xxl).
- responsiveMenuTitle (string, optional): The title to be displayed at the top of the container in responsiveMenu mode. If provided, it will replace the regular title when the viewport size matches the responsiveMenuSize.
- responsiveMenuPlacement (string, optional): The placement of the responsiveMenu offcanvas container. Can be 'start' or 'end'. Default is 'end'.
- responsiveMenuBodyClass (string, optional): Additional CSS classes to be applied to the offcanvas-body in responsiveMenu mode.
- responsiveMenuClose (boolean, optional) : If true, close by default the offcanvas menu. Default is false.
-->
<#macro pageColumn id='' width='' class='' height='' title='' flush=false center=false responsiveMenuSize='' responsiveMenuTitle=title responsiveMenuPlacement='end' responsiveMenuBodyClass='' responsiveMenuClose=false >
	<#if class?contains("p-")>
		<#assign classValue = class>
	<#else>
		<#if flush>
			<#assign classValue = class>
		<#else>
			<#assign classValue = class + " p-2 p-md-4">
		</#if>
	</#if>
    <#if responsiveMenuSize != '' || responsiveMenuClose >
    <div class="<#if responsiveMenuClose>offcanvas<#else>offcanvas-${responsiveMenuSize}</#if> offcanvas-${responsiveMenuPlacement} w-auto border-end overflow-x-hidden" style="<#if width != ''>min-width:${width}</#if>" tabindex="-1" <#if id != ''> id="${id}"</#if>>
        <div class="offcanvas-header border-bottom text-break px-4">
            <h2 class="offcanvas-title fw-bolder me-5" id="template-create-page-roleLabel">${responsiveMenuTitle}</h2>
            <button type="button" class="border btn btn-light btn-rounded btn-icon position-absolute end-0 me-4" data-bs-dismiss="offcanvas" data-bs-target="#<#if id != ''>${id}</#if>" aria-label="Close">
                <i class="ti ti-x fs-5"></i>
            </button>
        </div>
        <div class="offcanvas-body ${responsiveMenuBodyClass} overflow-hidden p-0">
    </#if>
    <div <#if id != '' && responsiveMenuSize=''>id="${id}"</#if> class="lutece-column <#if width = '' >w-100<#else>border-start border-end</#if> overflow-auto ${classValue}" style="<#if width != '' >width:${width};min-width:${width};</#if>">
	    <div class="<#if center>d-flex flex-column justify-content-center align-items-center h-100<#else> container-fluid scrollable</#if><#if flush> p-0 m-0</#if>">
            <#if title!=''><h1 class="fw-bolder mb-4 <#if responsiveMenuSize !=''>d-none d-${responsiveMenuSize}-block</#if>">${title}</h1></#if>
            <#nested>
        </div>
    </div>
    <#if responsiveMenuSize != ''>
    </div>
    </div>
    </#if>
</#macro>
