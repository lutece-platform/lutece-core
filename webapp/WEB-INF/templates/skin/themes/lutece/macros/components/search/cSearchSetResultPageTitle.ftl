<#-- Macro: cSearchSetResultPageTitle

Description: Modifie le titre de la page en fonction de la query de recherche.

-->  
<#macro cSearchSetResultPageTitle >
<script>
<#if query?? && query!=''>
window.addEventListener( "load", function() {
    const pageTitle = document.querySelector('title');
    const title = pageTitle.textContent
    pageTitle.textContent = `<#noparse>${title}</#noparse> - ${query!}`;
    <#nested>
})
</#if>
</script>
</#macro>