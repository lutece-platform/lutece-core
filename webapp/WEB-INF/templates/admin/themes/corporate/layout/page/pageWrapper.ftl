<#--
Macro: pageWrapper
Description: Generates a main container element for a page with a flexible width that fills the available space.
-->
<#macro pageWrapper template=''>
<!-- Begin layout content -->
<div id="lutece-page-wrapper" class="lutece-page-wrapper ${template}">
<#nested>
</div>
</#macro>