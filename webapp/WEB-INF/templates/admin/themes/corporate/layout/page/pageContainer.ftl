<#--
Macro: page
Description: Contenu principe
-->
<#macro pageContainer id='' template='' height="" class="">
<!-- Begin page content -->
<main role="main" <#if id!=''> id="${id}"</#if>  class="lutece-page ${template} d-flex ${class}" style="<#if height='full'>height:100%;max-height:100%</#if>">
<#nested>
</main>
<#if height='full'>
<style>
footer {
	display: none;
}
#lutece-layout-wrapper {
	display:block;
}
</style>
</#if>
</#macro>