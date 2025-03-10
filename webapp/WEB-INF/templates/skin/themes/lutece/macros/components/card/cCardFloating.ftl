<#-- Macro: cCardFloating

Description: affiche une carte flottante au dessus d'un contenu.

Parameters:

@param - id - string - required - l'ID de la carte
@param - title - string - optional - définit le titre de la carte
@param - titleLevel - number - optional - défaut 3 - définit le niveau de titre de la carte, pour compatibilité d'accessibilité
@param - class - string - optional - permet d'ajouter une classe CSS à la carte
@param - dismissible - boolean - optional - permet d'activer la fermeture de la carte (par défaut: true)
@param - params - string - optional - permet d'ajouter des parametres HTML à la carte
 -->
<#macro cCardFloating id title='' titleLevel=3 class='' dismissible=true params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card card-floating<#if class!=''> ${class!}</#if>" id="${id!}-card" <#if params!=''> ${params!}</#if>>
    <div class="card-body">
    	<div class='card-title-container'>
    		<@cTitle level=titleLevel class='card-title'>${title!}</@cTitle>
    		<#if dismissible>
              <button type="button" class="btn-close" aria-label="#i18n{theme.labelClose}" data-bs-target="#${id!}-card" data-bs-dismiss="alert" />
              </button>
            </#if>
       	</div>
        <#if title!=''></#if>
        <#nested>
    </div>
</div>
</#macro>