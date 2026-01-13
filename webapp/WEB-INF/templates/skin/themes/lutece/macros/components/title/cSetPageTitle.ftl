<#-- Macro: cSetPageTitle

Description: Modifie le titre de la page .

Parameters:
@param - title - string - mandatory - Titre de la page
@param - init - boolean - mandatory - Indique si c'est une initialisation on efface totalement le titre (true) ou une modification (false) on ajoute à la fin du titre
@param - srcElement - string - optional - default '' Element source pour le titre complémentaire
@param - type - string - optional -  default 'text' Type de l'élément source, texte ou input
-->  
<#macro cSetPageTitle title srcElement='' init=false type='text' deprecated...>
<@deprecatedWarning args=deprecated />
<#if title?? && title!=''>
const pageTitle = document.querySelector('title');
<#if init>
pageTitle.textContent = '${title}';
<#else>
const mainTitleText = pageTitle.textContent;
const complementaryTitleText = <#if srcElement=''>'${title!}'<#else><#if type='text'>document.querySelector('${srcElement}').textContent<#else>document.querySelector('${srcElement}').value</#if></#if>;
pageTitle.textContent = `<#noparse>${mainTitleText} ${complementaryTitleText}</#noparse>`;
</#if>
<#nested>
</#if>
</#macro>