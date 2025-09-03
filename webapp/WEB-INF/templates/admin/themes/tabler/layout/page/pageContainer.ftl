<#--
Macro: page
Description: Contenu principe
-->
<#macro pageContainer id='lutece-main' template='' height='' class='' actions='' params='' deprecated...> 
<@deprecatedWarning args=deprecated />
<!-- Begin page content -->
<main role="main" <#if id!=''> id="${id}"</#if>  class="lutece-page ${template} ${class} d-flex" style="<#if height='full'>height:calc(100%-64px);max-height:calc(100%-64px)</#if>" ${params!}>
<#nested>
</main>
<#if height='full'>
<style>
footer {display: none;}
</style>
</#if>
<#if actions!=''>
<script>
document.addEventListener( "DOMContentLoaded", function() {
    const actions = `${actions}`;
    document.querySelector('.page-header-buttons').innerHTML = actions;
});
</script>
</#if>
</#macro>