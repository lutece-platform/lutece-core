<#-- Macro: cBtn

Description: affiche un bouton interactif.

Parameters:
@param - id - string - optional - l'ID du bouton
@param - label - string - required - le titre du bouton
@param - class - string - optional - permet d'ajouter une classe CSS au bouton (valeur existantes dans le CSS: 'primary', 'secondary', 'tertiary', 'danger', 'expand')
@param - btnClass - string - optional - permet d'ajouter une classe CSS au label du bouton
@param - noclass - boolean - optional - permet de supprimer les classes CSS 'btn-' du bouton si pas de href (par défaut: false)
@param - href - string - optional - si valeur, permet de transformer le bouton en lien avec un attribut href
@param - type - string - optional - permet de modifier le type de bouton (par défaut: 'submit')
@param - nestedPos - string - optional - permet de gérer la position du contenu de la macro (par défaut: 'before', valeurs possibles: 'before', 'after')
@param - disabled - boolean - optional - permet de désactiver le bouton (par défaut: false)
@param - size - string - optional - permet de choisir la taille du bouton (choix possible: 'mini')
@param - params - string - optional - permet d'ajouter des parametres HTML au bouton
 -->
<#macro cBtn label class='primary' btnClass='' noclass=false href='' id='' params='' type='submit' nestedPos='before' disabled=false size='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if href=''>
<button class="<#if !noclass>btn btn-</#if>${class!}<#if size == 'mini'> btn-mini</#if>" type="${type!}"<#if id!=''> id="${id!}"</#if><#if params!=''>${params!}</#if><#if disabled> disabled</#if>>
<#if nestedPos='before'><#nested></#if><#if label!=''><span class="btn-label ${btnClass!}">${label!}</span></#if><#if nestedPos='after'><#nested></#if>
</button>
<#else>
<a href="${href!}" class="btn btn-${class!}" <#if id!=''> id="${id!}"</#if><#if params!=''>${params!}</#if><#if disabled> disabled</#if>> 
<#if nestedPos='before'><#nested></#if><#if label!=''><span class="btn-label ${btnClass!}">${label!}</span></#if><#if nestedPos='after'><#nested></#if>
</a>
</#if>
</#macro>