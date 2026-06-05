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
<#macro cTable caption='' captionClass='visually-hidden' summary='' breakpoint='' themed=true collapsible=true collapsed=false collapseHeader=true collapseFooter=true collapsedClass='' class='' id='changeme' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if id='changeme'><!-- ATTENTION : Risque de duplication d'id ! --></#if>
<div class="<#if id!=''>d-none d-md-block </#if>table-responsive<#if breakpoint!=''>${breakpoint}</#if>">
<table class="table<#if themed> ctable<#if collapsible> xs-collapsed</#if></#if><#if class !=''> ${class}</#if>"<#if summary !=''> summary="${summary!}"</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params}</#if> >
<#if caption !=''> <caption class="visually-hidden">${caption!}</caption></#if>
<#nested>
</table>
</div>
<#if id !=''><@cTableResponsive id=id collapsed=collapsed collapsedClass=collapsedClass collapseHeader=collapseHeader collapseFooter=collapseFooter/></#if>
</#macro>
<#macro sortSite jsp_url attribute asc=false desc=true id="" >
<#if jsp_url?contains("?")><#assign sort_url = jsp_url + "&amp;sorted_attribute_name=" + attribute + "&amp;asc_sort=" /><#else><#assign sort_url = jsp_url + "?sorted_attribute_name=" + attribute + "&amp;asc_sort=" /></#if>
<#if asc>
<a id="sort${id!}_${attribute!}" href="${sort_url}true#sort${id!}_${attribute!}" title="#i18n{portal.util.sort.asc}" class="btn-sort text-decoration-none" role="button">
	<@cIcon name='arrow-up' class='main-color' />
</a>
</#if>
<#if desc>
<a href="${sort_url}false#sort${id!}_${attribute!}" title="#i18n{portal.util.sort.desc}" class="btn-sort text-decoration-none" role="button">
	<@cIcon name='arrow-down' class='main-color' />
</a>
</#if>
</#macro>
<#macro cTableResponsive id collapsedClass='' collapsed=true collapseHeader=true collapseFooter=true >
<div class="d-md-none" id="accordion-table-${id!}"></div>
<script>
document.addEventListener('DOMContentLoaded', function() {
    <#if id='changeme'>console.warn( "macro cTable : ATTENTION : Risque de duplication. Modifier l'id de la macro cTable !" )</#if>
    const rows = document.querySelectorAll('table#${id} tbody tr');
    rows.forEach(function(row, index) {
        const cells = row.querySelectorAll('td');
        let accordionContent = '';
        cells.forEach(function(cell, i) {
            const headerSelector = document.querySelector('table#${id} thead th:nth-child(' + (i + 1) + ')')
            const headerText = headerSelector.querySelector('a:not(.btn-sort), button') ? headerSelector.innerHTML : headerSelector.textContent;
            const cellText = cell.getHTML();
            const cellClass = cell.className;
            <#noparse>accordionContent += `<p class="fw-bold">${headerText}</p><p class='mb-4 ${cellClass}'>${cellText}</p>`;</#noparse>
        });
        
        const accordionItem = `
            <@cAccordion id='heading${id}` + index + `' title='` + cells[0].textContent + `' btnClass='btn-outline-action' state=collapsed bodyClass=collapsedClass collapseHeader=collapseHeader collapseFooter=collapseFooter>
                ` + accordionContent + `
            </@cAccordion>`;

        document.getElementById('accordion-table-${id}').insertAdjacentHTML('beforeend', accordionItem);
    });
});
</script>
</#macro>
