<#--
Macro: pageColumn
Description: Generates a container element for a full-height column on a page with an optional fixed width and custom CSS classes. The container includes a nested container for page content.
Parameters:
- id (string, optional): the ID of the container element.
- width (string, optional): the width of the container element;
- class (string, optional): the CSS class of the container element.
- position (string, optional): the position of the container element (e.g. "fixed", "absolute", etc.).
-->
<#macro pageColumn id='' width='' class='' height=''>
<div class="<#if width = '' >w-100<#else>border-start border-end</#if> overflow-auto ${class}" style="<#if width != '' >width:${width};</#if><#if height='full'>height:calc(100vh - 64px);max-height:calc(100vh - 64px)</#if>">
<div class="container-fluid scrollable">
	<#nested>
</div>
</div>
</#macro>