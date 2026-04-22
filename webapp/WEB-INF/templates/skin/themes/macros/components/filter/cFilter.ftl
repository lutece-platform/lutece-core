<#--
Macro: cFilter

Description: Generates a toggleable filter panel with checkbox filters, search and clear buttons, and a result count display.

Parameters:
- checkboxes (object, required): List of filter items, each with 'title' and 'id' attributes.
- id (string, optional): The unique identifier for the filter container. Default: ''.
- result (string, optional): Text displaying the filter result count. Default: ''.
- fieldSetLabel (string, optional): The legend text for the filter fieldset. Default: 'Liste de filtres'.
- showLegend (boolean, optional): If true, the fieldset legend is visible. Default: false.
- class (string, optional): Additional CSS class(es) for the filter container. Default: ''.
- params (string, optional): Additional HTML attributes for the filter container. Default: ''.

Showcase:
- desc: Filtre - @cFilter
- newFeature: false

Snippet:

    Filter panel with checkboxes:

    <@cFilter checkboxes=[
        {'id': 'filter-parks', 'title': 'Parks'},
        {'id': 'filter-libraries', 'title': 'Libraries'},
        {'id': 'filter-pools', 'title': 'Swimming pools'}
    ] result='12 results found' />

-->
<#macro cFilter checkboxes id='' result='' fieldSetLabel='Liste de filtres' showLegend=false class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cContainer id>
	<@cRow class='justify-content-between align-items-center'>
		<@cText type='span' class='filter-result'>${result!}</@cText>
		<@cBtn label='Masquer les filtres' id='filter-toggle-button' class='secondary m-1 d-flex align-items-center w-auto' >
			<@cIcon name='filter' title='Filter' class='main-info-color me-3' />
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