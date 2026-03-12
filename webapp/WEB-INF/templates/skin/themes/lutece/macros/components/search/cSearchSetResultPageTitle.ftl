<#--
Macro: cSearchSetResultPageTitle

Description: Generates a JavaScript snippet that appends the search query to the page title for search result pages.

Snippet:

    Basic usage (place on search result page):

    <@cSearchSetResultPageTitle />

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