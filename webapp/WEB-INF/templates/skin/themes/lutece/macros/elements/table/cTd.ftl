<#-- Macro: cTd

Description: définit une cellule du tableau.

Parameters:
@param - id - string - optional - l'ID de la cellule du tableau
@param - class - string - optional - permet d'ajouter une classe CSS à la cellule du tableau
@param - scope - string - optional - permet de définir le scope de la cellule (valeurs possibles: 'col', 'row') 
@param - role - string - optional - permet de définir le role de la cellule du tableau
@param - headers - string - optional - permet de spécifier la liste des identifiants des cellules d'en-tête, th, qui fournissent des informations d'en-tête pour la cellule de données actuelles
@param - colspan - string - optional - permet de définir l'attribut 'colspan'
@param - nospan - boolean - optional - permet d'ajouter un tag 'span' dans le 'td' (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML à la cellule du tableau
-->
<#macro cTd id='' class='' scope='' headers='' role='' colspan='' nospan=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<td<#if scope!=''> scope="${scope!}"</#if><#if colspan!=''> colspan="${colspan!}"</#if><#if headers!=''> headers="${headers!}"</#if><#if class != ''> class="${class}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if><#if role!=''> role="${role!}"</#if>>
<#if !nospan><span><#nested></span><#else><#nested></#if>
</td>
</#macro>