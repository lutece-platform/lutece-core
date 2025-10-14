<#-- 
Macro: table

Description: Generates an HTML table with an optional ID, class, and various other features.

Parameters:
- id (string, optional): the ID of the table.
- class (string, optional): the class of the table.
- responsive (boolean, optional): whether to make the table responsive.
- condensed (boolean, optional): whether to make the table cells more compact.
- hover (boolean, optional): whether to highlight rows on hover.
- striped (boolean, optional): whether to add stripes to the table rows.
- headBody (boolean, optional): whether to include the <thead> and <tbody> tags.
- bordered (boolean, optional): whether to add borders to the table cells.
- narrow (boolean, optional): whether to make the table cells narrower.
- collapsed (boolean, optional): whether to collapse the table.
- caption (string, optional): the caption for the table.
- params (string, optional): additional parameters to add to the HTML code.
-->
<#macro table id='' class='' responsive=true condensed=true hover=true striped=false headBody=false bordered=false narrow=false collapsed=false caption='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local class = class />
<#if condensed> <#local class += ' table-condensed' /> </#if>
<#if hover>     <#local class += ' table-hover' /> </#if>
<#if striped>   <#local class += ' table-striped'   /> </#if>
<#if bordered>  <#local class += ' table-bordered'  /> </#if>
<#if collapsed> <#local class += ' collapse' /> </#if>
<#if responsive><div class="table-responsive"></#if>
<table class="table ${class?trim}" <#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#if caption!=''><caption>${caption}</caption></#if>
<#if headBody><thead></#if>
	<#nested>
<#if headBody></tbody></#if>
</table>
<#if responsive></div></#if>
</#macro>