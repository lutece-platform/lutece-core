<#-- Macro: cCascading

Description: affiche une liste d'élements empilés verticalement. Au clic révèle ou masque le contenu associé.

Parameters:

@param - title - string - required - permet de définir le titre de la cascade
@param - id - string - optional - l'ID de la cascade
@param - class - string - optional - permet d'ajouter une classe CSS à la cascade
@param - state - boolean - optional - permet de définir si la cascade est ouverte au chargement de la page (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML à la cascade
 -->
<#macro cCascading title id='' class='' params='' state=false >
<details<#if id !=''> id='${id}'</#if> class="cascading ${class!}" <#if state> open</#if> aria-expanded="<#if state>true<#else>false</#if>" ${params}>
  <summary><span class="cascading-label">${title}</span></summary>
  <div class="cascading-content"><#nested></div>
</details>
</#macro>