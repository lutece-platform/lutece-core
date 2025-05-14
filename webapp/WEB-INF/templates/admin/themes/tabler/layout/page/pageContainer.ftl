<#--
Macro: page
Description: Contenu principe
-->
<#macro pageContainer id='lutece-main' template='' height='' class='' params='' deprecated...> 
<!-- Begin page content -->
<main role="main" <#if id!=''> id="${id}"</#if>  class="lutece-page ${template!} d-flex ${class!}" <#if height='full'>style="height:calc(100%-64px);max-height:calc(100%-64px)"</#if> ${params!}>
<#nested>
</main>
<#if height='full'>
<style>
footer {
	display: none;
}
</style>
</#if>
</#macro>