<#-- Macro: cTabs

Description: permet d'afficher des onglets comprenant un contenu.

Parameters:

@param - id - string - optional - l'ID des onglets
@param - class - string - optional - ajoute une classe CSS aux onglets
@param - navigation - boolean - optional - permet d'intégrer les onglets dans une balise HTML 'nav'
@param - params - string - optional - permet d'ajouter des parametres HTML aux onglets
-->
<#macro cTabs navigation=false id='' class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if navigation>
	<nav>
		<div class="nav-tabs-container">
			<ul class="nav nav-tabs<#if class!=''> ${class!}</#if>"<#if id !=''> id="${id!}"</#if> role="tablist" <#if params!=''>${params!}</#if>>
				<#nested>
			</ul>
		</div>
	</nav>
<#else>
	<div class="nav-tabs-container">
		<div class="nav nav-tabs<#if class!=''> ${class!}</#if>"<#if id !=''> id="${id!}"</#if> role="tablist" <#if params!=''>${params!}</#if>>
			<#nested>
		</div>
	</div>
</#if>
</#macro>