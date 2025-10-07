<#-- Macro: cFilter

Description: permet de créer filtre avec bouton pour cacher le filtre, des boutons de gestion, et un resultat.

Parameters:

@param - id - string - optional - l'ID du filtre
@param - class - string - optional - ajoute une classe CSS au filtre
@param - checkboxes - object - required - Objet list avec items de filtre. L'objet contient un attribut 'title' et un attribut 'id'
@param - result - string - optional - permet d'afficher le resultat du filtre avec une variable dynamique
@param - fieldSetLabel - string - optional - permet de définir le libellé du fieldset des filtres (par défaut: 'Liste de filtres')
@param - showLegend - booelan - optional - permet si la légende est affichée, par défaut false
@param - params - string - optional - permet d'ajouter des parametres HTML au filtre
-->
<#macro cFilter checkboxes id='' result='' fieldSetLabel='Liste de filtres' showLegend=false class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cContainer id>
	<@cRow class='justify-content-between align-items-center'>
		<@cText type='span' class='filter-result'>${result!}</@cText>
		<@cBtn label='Masquer les filtres' id='filter-toggle-button' class='secondary m-1 d-flex align-items-center w-auto' >
			<@parisIcon name='filter' title='Filter' class='main-info-color me-3' />
		</@cBtn>
	</@cRow>
	<@cRow id='filters-container' class=class! params=params!>
		<#local legendClass><#if !showLegend>visually-hidden</#if></#local>
		<@cFieldset legend=fieldSetLabel legendClass=legendClass class="d-flex m-0 flex-wrap w-auto">
			<#list checkboxes as checkbox>
		        <@cField class='mt-0'>
		            <@cCheckbox class='me-3 pt-3 pb-4 px-3 form-check' name='selectCheckbox' id='${checkbox.id!}' label='${checkbox.title!}' value='filter' selectionButton=true />
		        </@cField>
		    </#list>
		</@cFieldset>
        <@cBtn label='Rechercher' class='primary h-100 w-auto' />
    	<@cBtn label='Tout effacer' id='filter-erase-button' class='tertiary h-100 w-auto' />
	</@cRow>
</@cContainer>
<script type="module" src="${commonsSiteThemePath}${commonsSiteJsPath}/modules/theme-filter.js"></script>
</#macro>