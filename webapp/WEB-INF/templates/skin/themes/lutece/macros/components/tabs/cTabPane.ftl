<#-- Macro: cTabPane

Description: permet de gérer le panneau d'un onglet.

Parameters:

@param - id - string - required - l'ID du panneau (doit etre similaire à celui de l'onglet referent) 
@param - class - string - optional - ajoute une classe CSS au panneau
@param - bodyClass - string - optional - ajoute une classe CSS au body
@param - title - string - optional - permet de définir le titre du panneau affiché en mobile
@param - titleLevel - number - optional - permet de définir le niveau de titre du panneau (par défaut: 3)
@param - active - boolean - optional - permet de définir si le panneau est actif au chargement de la page (par défaut: false)
@param - disabled - boolean - optional - permet de définir si le panneau est désactivé (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML à l'onglet
-->
<#macro cTabPane id active=false disabled=false class='' bodyClass='' title='' titleLevel=3 params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card tab-pane fade<#if active> show active</#if><#if disabled> disabled</#if><#if class!=''> ${class!}</#if>"<#if disabled> disabled aria-disabled="true"</#if> id="${id}" tabindex="0" role="tabpanel" aria-labelledby="tab_${id}" ${params!}>    
	<div class="card-body<#if bodyClass!=''> ${bodyClass!}</#if>">
		<#nested>
	</div>
</div>
</#macro>