<#-- Macro: cAnchor

Description: permet de créer une liste d'ancres sur une page.

Parameters:

@param - id - string - optional - l'ID de la liste d'ancres
@param - class - string - optional - ajoute une classe CSS au container de la liste d'ancre
@param - listClass - string - optional - ajoute une classe CSS au à la liste
@param - listItemClass - string - optional - ajoute une classe CSS au éléments de la liste
@param - anchorsClass - string - optional - ajoute une classe CSS aux ancres
@param - anchors - object - required - Objet list avec items d'ancres. L'objet contient un attribut 'href' et un attribut 'label'
@param - params - string - optional - permet d'ajouter des parametres HTML à l'ancre
-->
<#macro cAnchor anchors id='' class='' listClass='' listItemClass='' anchorsClass='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cBlock id=id! class='anchors ${class}'>
	<@chList class=listClass >
		<#list anchors as anchor>
			<@chItem class=listItemClass>
				<@cLink href=anchor.href! label=anchor.label! class=anchorsClass! />
			</@chItem>
		</#list>
	</@chList>
</@cBlock>
</#macro>