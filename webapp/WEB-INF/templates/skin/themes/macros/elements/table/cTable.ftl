<#--
Macro: cTable

Description: Generates a themed responsive content table with a caption, optional Bootstrap 5 fallback styling, and an automatic accordion variant on small screens (via the internal `cTableResponsive` macro). The accordion variant is keyed by `id` and built at runtime from the `<thead>` / `<tbody>` cells.

Parameters:
- caption (string, optional): table caption text (rendered visually hidden inside `<caption>`). Default: ''.
- captionClass (string, optional): CSS class applied to the `<caption>`. Use 'visually-hidden' to hide it from sighted users (useful when a heading precedes the table). Default: 'visually-hidden'.
- summary (string, optional): value of the `summary` attribute on the `<table>`. Default: ''.
- breakpoint (string, optional): breakpoint suffix for the responsive wrapper. Accepted values: '', '-sm', '-md', '-lg', '-xl', '-xxl'. Default: ''.
- themed (boolean, optional): apply the theme's `ctable` class. If false, Bootstrap 5 default table styling is used. Default: true.
- collapsible (boolean, optional): on small screens, hide all cells except the first column. Requires `themed=true`. Default: true.
- collapsed (boolean, optional): when the accordion variant is shown on mobile, whether the panels are initially collapsed. Default: false.
- collapsedTitleLevel (number, optional): heading level of the accordion titles in the mobile variant. Default: 3.
- collapseHeader (boolean, optional): show the accordion header in the mobile variant. Default: true.
- collapseFooter (boolean, optional): show the accordion footer in the mobile variant. Default: true.
- collapsedClass (string, optional): CSS class applied to the mobile accordion body. Default: ''.
- class (string, optional): additional CSS class on the `<table>`. Default: ''.
- id (string, optional): the ID of the `<table>` and the seed for the accordion variant. **WARNING**: the default 'changeme' triggers a duplicate-ID risk warning — always set a unique id when using `cTableResponsive`. Default: 'changeme'.
- params (string, optional): additional HTML attributes for the `<table>`. Default: ''.

Snippet:

    Basic themed table with caption:

    <@cTable id='users-table' caption='Liste des utilisateurs' themed=true>
        <@cThead>
            <@cTr>
                <@cTh>Nom</@cTh>
                <@cTh>Email</@cTh>
                <@cTh>Rôle</@cTh>
            </@cTr>
        </@cThead>
        <@cTbody>
            <#list users as user>
                <@cTr>
                    <@cTd>${user.name}</@cTd>
                    <@cTd>${user.email}</@cTd>
                    <@cTd>${user.role}</@cTd>
                </@cTr>
            </#list>
        </@cTbody>
    </@cTable>

    Plain Bootstrap 5 table (no theme, no responsive accordion):

    <@cTable id='data-table' themed=false collapsible=false>
        <@cThead>...</@cThead>
        <@cTbody>...</@cTbody>
    </@cTable>

    Themed table with large-screen breakpoint and pre-expanded accordion on mobile:

    <@cTable id='orders' caption='Commandes' breakpoint='-lg' collapsed=false collapsedClass='bg-light'>
        ...
    </@cTable>

-->
<#macro cTable caption='' captionClass='visually-hidden' summary='' breakpoint='' themed=true collapsedTitleLevel=3 collapsible=true collapsed=false collapseHeader=true collapseFooter=true collapsedClass='' class='' id='changeme' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if id='changeme'><!-- #i18n{portal.theme.msgInfoTableUpdateId} --></#if>
<div class="table-responsive<#if breakpoint!=''>${breakpoint}</#if>"<#if id!=''> id="table-wrapper-${id}"</#if>>
<table class="table<#if themed> ctable<#if collapsible> xs-collapsed</#if></#if><#if class !=''> ${class}</#if>"<#if summary !=''> summary="${summary!}"</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params}</#if> >
<#if caption !=''> <caption class="visually-hidden">${caption!}</caption></#if>
<#nested>
</table>
</div>
<#if id !=''><@cTableResponsive id=id collapsed=collapsed collapsedClass=collapsedClass collapsedTitleLevel=collapsedTitleLevel collapseHeader=collapseHeader collapseFooter=collapseFooter/></#if>
</#macro>
<#macro sortSite jsp_url attribute asc=false desc=true id="" >
<#if jsp_url?contains("?")><#assign sort_url = jsp_url + "&amp;sorted_attribute_name=" + attribute + "&amp;asc_sort=" /><#else><#assign sort_url = jsp_url + "?sorted_attribute_name=" + attribute + "&amp;asc_sort=" /></#if>
<#if asc>
<button type="button" id="sort${id!}_${attribute!}" onclick="window.location.href='${sort_url}true#sort${id!}_${attribute!}'" title="#i18n{portal.util.sort.asc}" aria-label="#i18n{portal.util.sort.asc}" class="btn-sort text-decoration-none">
	<@cIcon name='arrow-up' class='main-color' />
</button>
</#if>
<#if desc>
<button type="button" onclick="window.location.href='${sort_url}false#sort${id!}_${attribute!}'" title="#i18n{portal.util.sort.desc}" aria-label="#i18n{portal.util.sort.desc}" class="btn-sort text-decoration-none">
	<@cIcon name='arrow-down' class='main-color' />
</button>
</#if>
</#macro>
<#macro cTableResponsive id collapsedClass='' collapsed=true collapsedTitleLevel=3 collapseHeader=true collapseFooter=true >
<#-- The accordion is hidden with the HTML "hidden" attribute rather than a CSS class, so that only one
     structure is exposed when CSS is off. The script switches between the table and the accordion
     at the md breakpoint (matchMedia), whatever the styles. -->
<div id="accordion-table-${id!}" hidden></div>
<script>
document.addEventListener('DOMContentLoaded', function() {
    <#if id='changeme'>console.warn( "#i18n{portal.theme.msgInfoTableUpdateId}" )</#if>
    const tableWrapper = document.getElementById('table-wrapper-${id}');
    const accordion = document.getElementById('accordion-table-${id}');
    const mql = window.matchMedia('(min-width: 768px)');
    let built = false;

    function buildAccordion() {
        const rows = document.querySelectorAll('table#${id} tbody tr');
        rows.forEach(function(row, index) {
            const cells = row.querySelectorAll('td');
            let accordionContent = '';
            cells.forEach(function(cell, i) {
                const headerSelector = document.querySelector('table#${id} thead th:nth-child(' + (i + 1) + ')')
                const headerText = headerSelector.querySelector('button:not(.btn-sort)') ? headerSelector.innerHTML : headerSelector.textContent;
                const cellText = cell.getHTML();
                const cellClass = cell.className;
                <#noparse>accordionContent += `<p class="fw-bold">${headerText}</p><p class='mb-4 ${cellClass}'>${cellText}</p>`;</#noparse>
            });

            const accordionItem = `
                <@cAccordion id='heading${id}` + index + `' title='` + cells[0].textContent + `' btnClass='btn-outline-action' titleLevel=collapsedTitleLevel state=collapsed bodyClass=collapsedClass collapseHeader=collapseHeader collapseFooter=collapseFooter>
                    ` + accordionContent + `
                </@cAccordion>`;

            accordion.insertAdjacentHTML('beforeend', accordionItem);
        });
        built = true;
    }

    function update(isDesktop) {
        if (isDesktop) {
            if (tableWrapper) tableWrapper.hidden = false;
            accordion.hidden = true;
        } else {
            // The accordion is built once, the first time the small screen layout is needed
            if (!built) buildAccordion();
            if (tableWrapper) tableWrapper.hidden = true;
            accordion.hidden = false;
        }
    }

    update(mql.matches);
    mql.addEventListener('change', function(e) { update(e.matches); });
});
</script>
</#macro>
