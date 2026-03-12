<#--
Macro: cTable

Description: Generates a responsive HTML table with theme styling, collapsible rows for small screens, and an accessible caption. Automatically creates an accordion view for mobile devices.

Parameters:
- caption (string, optional): Caption text for the table, used for accessibility. Default: ''.
- captionClass (string, optional): CSS class for the caption element. Default: 'visually-hidden'.
- summary (string, optional): Summary attribute for the table. Default: ''.
- breakpoint (string, optional): Responsive breakpoint suffix ('-sm', '-md', '-lg', '-xl', '-xxl'). Default: ''.
- themed (boolean, optional): If true, applies the theme default table styles; otherwise Bootstrap styles apply. Default: true.
- collapsible (boolean, optional): If true, collapses rows on small screens showing only the first cell. Default: true.
- class (string, optional): CSS class(es) applied to the table element. Default: ''.
- id (string, optional): Unique identifier for the table; must be changed from default to avoid conflicts. Default: 'changeme'.
- params (string, optional): Additional HTML attributes for the table element. Default: ''.

Showcase:
- desc: Tableau - @cTable
- bs: content/tables
- newFeature: false

Snippet:

    Basic usage:

    <@cTable caption='List of services' id='services-table'>
        <@cThead>
            <@cTr>
                <@cTh>Name</@cTh>
                <@cTh>Description</@cTh>
            </@cTr>
        </@cThead>
        <@cTbody>
            <@cTr>
                <@cTd>Civil Status</@cTd>
                <@cTd>Request birth or marriage certificates</@cTd>
            </@cTr>
        </@cTbody>
    </@cTable>

-->  
<#macro cTable caption='' captionClass='visually-hidden' summary='' breakpoint='' themed=true collapsible=true class='' id='changeme' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if id='changeme'><!-- ATTENTION : Risque de duplication d'id ! --></#if>
<div class="<#if id!=''>d-none d-md-block </#if>table-responsive<#if breakpoint!=''>${breakpoint}</#if>">
<table class="table<#if themed> ctable<#if collapsible> xs-collapsed</#if></#if><#if class !=''> ${class}</#if>"<#if summary !=''> summary="${summary!}"</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params}</#if> >
<#if caption !=''> <caption class="visually-hidden">${caption!}</caption></#if>
<#nested>
</table>
</div>
<#if id !=''><@cTableResponsive id=id /></#if>
</#macro>
<#macro sortSite jsp_url attribute asc=false desc=true id="" >
<#if jsp_url?contains("?")><#assign sort_url = jsp_url + "&amp;sorted_attribute_name=" + attribute + "&amp;asc_sort=" /><#else><#assign sort_url = jsp_url + "?sorted_attribute_name=" + attribute + "&amp;asc_sort=" /></#if>
<#if asc>
<a id="sort${id!}_${attribute!}" href="${sort_url}true#sort${id!}_${attribute!}" title="#i18n{portal.util.sort.asc}" class="text-decoration-none" role="button">
	<@parisIcon name='arrow-up' class='main-color' />
</a>
</#if>
<#if desc>
<a href="${sort_url}false#sort${id!}_${attribute!}" title="#i18n{portal.util.sort.desc}" class="text-decoration-none" role="button">
	<@parisIcon name='arrow-down' class='main-color' />
</a>
</#if>
</#macro>
<#macro cTableResponsive id>
<div class="d-md-none" id="accordion-table-${id!}"></div>
<script>
document.addEventListener('DOMContentLoaded', function() {
    <#if id='changeme'>console.warn( ' [Thème Paris fr] macro cTable : ATTENTION : Risque de duplication d\'id. Modifier l\'id de la macro cTable !' )</#if>
    const rows = document.querySelectorAll('table#${id} tbody tr');
    rows.forEach(function(row, index) {
        const cells = row.querySelectorAll('td');
        let accordionContent = '';

        cells.forEach(function(cell, i) {
            const headerText = document.querySelector('table#${id} thead th:nth-child(' + (i + 1) + ')').textContent;
            const cellText = cell.textContent;
            <#noparse>accordionContent += `<p>${headerText}</p><p class='mb-4'><strong>${cellText}</strong></p>`;</#noparse>
        });

        const accordionItem = `
            <@cAccordion id='heading${id}` + index + `' title='` + cells[0].textContent + `' btnClass='btn-outline-action' state=false params="class='m-0'">
                ` + accordionContent + `
            </@cAccordion>`;

        document.getElementById('accordion-table-${id}').insertAdjacentHTML('beforeend', accordionItem);
    });
});
</script>
</#macro>
