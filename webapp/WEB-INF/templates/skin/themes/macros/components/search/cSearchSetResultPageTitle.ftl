<#--
Macro: cSearchSetResultPageTitle

Description: Generates a JavaScript snippet that appends the current search query to the document title on search result pages (format: "Original Title - query"). Reads the `query` variable from the page model and updates the `<title>` element after page load. Supports nested content for additional JS logic to run after the title is updated.

Parameters: None. The macro reads the `query` variable from the model — no value is rendered if `query` is undefined or empty. Nested content (optional) is inserted inside the `load` event listener after the title is updated.

Snippet:

    Basic usage (place on a search result page where `query` is set in the model):

    <@cSearchSetResultPageTitle />

    With nested JS to track the search query in analytics after title update:

    <@cSearchSetResultPageTitle>
        if (typeof gtag === 'function') {
            gtag('event', 'search', { search_term: '${query!}' });
        }
    </@cSearchSetResultPageTitle>

-->  
<#macro cSearchSetResultPageTitle >
<#if query?? && query?has_content>
<div id="searchQuery" hidden data-query="${query!?html}"></div>
<script>
window.addEventListener("load", function () {
    var q = document.getElementById('searchQuery').dataset.query;
    var t = document.querySelector('title');
    t.textContent = t.textContent + ' - ' + q;
    <#nested>
});
</script>
</#if>
</#macro>