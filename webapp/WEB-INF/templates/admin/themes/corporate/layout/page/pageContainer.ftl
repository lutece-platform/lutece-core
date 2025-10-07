<#--
Macro: page
Description: Contenu principe
-->
<#macro pageContainer id='' template='' height='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<!-- Begin page content -->
<div <#if id!=''>id="${id}" </#if>class="lutece-page d-flex<#if template!=''> ${template}</#if><#if class!=''> ${class}</#if>"<#if height='full'> style="height:100%;max-height:100%"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
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