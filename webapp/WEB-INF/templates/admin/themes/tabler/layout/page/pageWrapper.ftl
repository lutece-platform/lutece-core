<#--
Macro: pageWrapper
Description: Generates a main container element for a page with a flexible width that fills the available space.
-->
<#macro pageWrapper class='' template='' deprecated...>
<@deprecatedWarning args=deprecated />
<!-- BEGIN PAGE BODY -->
<div class="page-body<#if class!=''> ${class}</#if>">
    <div class="container-xl ${template}">
    <#nested>
    </div>
</div>
</#macro>