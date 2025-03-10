<#-- Macro: cBtnGroup

Description: affiche un groupe de bouton.

Parameters:

@param - id - string - optional - l'ID du groupe de bouton
@param - label - string - required - définit l'aria-label du groupe de bouton
@param - buttonList - object - optionnel - Objet list avec items de bouton. L'objet doit contenir un attribut 'label', un attribut 'class' et un attribut optionnel 'disabled'
@param - class - string - optional - permet d'ajouter une classe CSS au groupe de bouton
@param - type - string - optional - permet de modifier le type de groupe de bouton (valeur possible: 'vertical')
@param - params - string - optional - permet d'ajouter des parametres HTML au groupe de bouton
 -->
<#macro cBtnGroup label buttonList={} class='' id='' type='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local btnClass>btn-group<#if type='vertical'> btn-group-vertical</#if><#if class!=''> ${class}</#if></#local>
<@cSection type='div' class=btnClass id=id params='${params} role="group" aria-label="${label}"'>
	<#if buttonList?has_content>
		<#if type == 'vertical'>
		    <@chList class="list-unstyled d-flex flex-column m-0">
		         <#list buttonList as button>
		                <@chItem class='m-0 d-flex' >
		                    <@cBtn label='${button.label}' class='${button.class} w-100' disabled=button.disabled />
		                </@chItem>
		         </#list>
		    </@chList>
		<#else>
		    <@chList class="list-unstyled d-flex flex-wrap m-0">
		         <#list buttonList as button>
		                <@chItem class='m-0 d-flex' >
		                    <@cBtn label='${button.label}' class='${button.class}' disabled=button.disabled />
		                </@chItem>
		         </#list>
		    </@chList>
		</#if>
	<#else>
		<#nested>
	</#if>
</@cSection>
</#macro>