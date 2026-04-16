<#--
Macro: cScrollSpy

Description: Permet de créer une liste d'ancres sur une page.

Parameters:
- anchorId  - required - l'ID de la liste d'ancres.
- anchors - required - Liste de sancres dan su ntableau avec des items en JSON. par exemple <#assign anchors = [{"label":"Item 1", "href":"jsp/site/Portal.jsp?page_id=6#anchor-list-item-xxs"},...]>
- anchorClass : - string - optional - ajoute une classe CSS au à la liste des acnres - par défaut "position-sticky"
- anchorListClass - string - optional - ajoute une classe CSS au à la liste
- anchorListItemClass - string - optional - ajoute une classe CSS au éléments de la liste
- anchorsLinkClass - string - optional - ajoute une classe CSS aux ancres
- anchorsParams - string - optional - Objet list avec items d'ancres. L'objet contient un attribut 'href' et un attribut 'label'
- anchorColClass - string - optional - Objet list avec items d'ancres. L'objet contient un attribut 'href' et un attribut 'label'
- contentColClass - string - optional - Objet list avec items d'ancres. L'objet contient un attribut 'href' et un attribut 'label'
- id - string - optional - l'Id du wrapper - .row- des colonnes
- class - string - optional - ajoute une classe CSS au wrapper
- params - string - optional - permet d'ajouter des parametres HTML au wrapper.

Snippet:
	<#assign anchorsList = [
		{"label":"Item 1", "href":"#anchor-scroll-item-1"},
		{"label":"Item 2", "href":"#anchor-scroll-item-2"},
		{"label":"Item 3", "href":"#anchor-scroll-item-3"},
		{"label":"Item 4", "href":"#anchor-scroll-item-4"},
		{"label":"Item 5", "href":"#anchor-scroll-item-5"}
	]>
	<@cScrollSpy anchorId='scrollspy-sample' anchors= anchorsLinkClass='btn btn-primary' anchorsParams='style="top:100px;"' />
-->
<#macro cScrollSpy anchorId anchors anchorClass='position-sticky' anchorListClass='list-unstyled' anchorListItemClass='ps-m py-s' anchorsLinkClass='' anchorsParams='' anchorColClass='col-12 col-md-3' contentColClass='col-12 col-md-9' id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="row<#if class!=''> ${class}</#if>"<#if id!=''> id="${class}"</#if><#if params!=''> ${params}</#if>>
	<div class="${anchorColClass}">
		<@cAnchor id=anchorId class=anchorClass anchors=anchors listClass=anchorListClass listItemClass=anchorListItemClass anchorsClass=anchorsLinkClass params=anchorsParams />
	</div>
	<div class="${contentColClass}">
		<div data-bs-spy="scroll" data-bs-target="#${anchorId}" tabindex="0"'>
			<#nested>
		</div>
	</div>
</div>
</#macro>