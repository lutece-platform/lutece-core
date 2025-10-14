<#-- Macro: cTable

Description: affiche un tableau de contenu.

Parameters:
@param - id - string - optional - Défaut "changeme", l'ID du table, ATTENTION changer l'identifiant par défaut si vous utilisez le macro cTableResponsive
@param - class - string - optional - permet d'ajouter une classe CSS au tableau
@param - caption - string - optional - permet de définir le titre "Caption" du tableau
@param - captionClass - string - optional - Défaut "visually-hidden" class du caption du tableau. Accessibilité: permet de masquer le titre du tableau pour les lecteurs d'écran si ele titre précédent le tableau est suffisant.
@param - summary - string - optional - permet de définir l'attribut "summary" du tableau
@param - breakpoint - string - optional - permet de définir le breakpoint du tableau (valeurs possibles: '-sm','-md','-lg','-xl','-xxl')
@param - themed - boolean - optional - permet d'ajouter les style du thème par défaut sinon ce sont les styles de table de BS4 qui seront appliqués (par défaut: true)
@param - collapsible - boolean - optional - par défaut pour les petits écran, seule la prmière cellule de la ligne est affichée -en colonne- et les autres sont masquée. Si false tout est en colonne sauf si le paramètre themed est false les styles BS4 seront alors appliqués par défauts (par défaut: true)
@param - params - string - optional - permet d'ajouter des parametres HTML au tableau
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
