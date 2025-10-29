<#-- Macro: cForm

Description: permet de définir le container d'un formulaire.

Parameters:

@param - id - string - optional - l'ID du formulaire
@param - class - string - optional - ajoute une classe CSS au formulaire
@param - method - string - optional - permet de définir la valeur de l'attribut 'method' du formulaire (par défaut: 'post')
@param - name - string - optional - permet de définir la valeur de l'attribut 'name' du formulaire
@param - role - string - optional - permet de définir la valeur de l'attribut 'aria-label' du formulaire
@param - action - string - optional - permet de définir l'url de l'action du formulaire
@param - params - string - optional - permet d'ajouter des parametres HTML au formulaire
-->
<#macro cForm class='' id='' params='' name='' method='post' role='' action='jsp/site/Portal.jsp' deprecated...>
<@deprecatedWarning args=deprecated />
<form <#if class!=''>class="${class}"</#if> <#if id!=''> id="${id!name}"</#if><#if action!=''> action="${action}"</#if><#if method!=''> method="${method}"</#if><#if name!=''> name="${name}"</#if><#if role!=''> aria-label="${role}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</form>
</#macro>