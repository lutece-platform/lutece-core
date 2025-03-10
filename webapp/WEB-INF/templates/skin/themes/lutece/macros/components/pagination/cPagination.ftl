<#-- Macro: cPagination

Description: permet de créer une pagination pour une liste d'éléments.

Parameters:

@param - id - string - optional - l'ID de la pagination  
@param - class - string - optional - ajoute une classe CSS à la pagination
@param - paginator - list - required - Objet Paginator du core LUTECE ou liste avec comme attributs les url et index de pagination.
@param - label - string - optional - permet de définir un libellé à la pagination (par défaut: 'Pagination')
@param - fragment - string - optional - permet d'ajouter un suffixe à l'url de la page
@param - params - string - optional - permet d'ajouter des parametres HTML à la pagination
-->
<#macro cPagination paginator label='Pagination' fragment='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="${label!}" <#if id!=''> id="${id}"</#if><#if class!=''> class="${class}"</#if><#if params!=''> ${params}</#if> >
<#if (paginator.pagesCount > 1) >
	<@cPaginationLinks paginator=paginator class=class fragment=fragment />
</#if>
</nav>
</#macro>