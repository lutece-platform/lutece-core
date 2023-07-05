<#--
Macro: pageColumn
Description: Generates a container element for a full-height column on a page with an optional fixed width and custom CSS classes. The container includes a nested container for page content.
Parameters:
- id (string, optional): The ID of the container element.
- width (string, optional): The width of the container element. Default is 100%.
- class (string, optional): The CSS class of the container element. Use "p-" prefix to add padding classes, e.g. "p-2" or "p-5".
- height (string, optional): The height of the container element. Use "full" to set the height to 100% of the viewport height.
- title (string, optional): The title to be displayed at the top of the container element. If provided, the container will have a fixed height and a scrollable content area.
-->
<#macro pageColumn id='' width='' class='' height='' title='' flush=false center=false>
<#if class?contains("p-")>
	<#assign classValue = class>
<#else>
	<#if flush>
		<#assign classValue = class>
	<#else>
		<#assign classValue = class + " p-5">
	</#if>
</#if>
<div<#if id != ''> id="${id}"</#if> class="lutece-column <#if width == ''>w-100<#else></#if> <#if title = ''>overflow-auto ${classValue}<#else>overflow-hidden p-0</#if>" style="<#if width != ''>width:${width};</#if><#if height=='full'>height:100%;max-height:100%</#if>">
	<#if title != ''>
		<div class="m-4">
			<div class="w-100 px-4 feature-group border-bottom">
				<h1 class="fw-bolder mb-0 h-60 lh-60">${title}</h1>
			</div>
			<#if !flush>
				<div class="container-fluid list-group list-group-flush scrollarea pb-5 px-4" style="height:calc(100vh - 180px)">
					<#nested>
				</div>
			<#else>
				<#nested>
			</#if>
		</div>
	<#else>
		<#if center>
			<div class="d-flex justify-content-center align-items-center h-100">
			<#nested>
			</div>
		<#else>
			<#nested>
		</#if>
	</#if>
</div>
</#macro>