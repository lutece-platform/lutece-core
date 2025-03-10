<#-- Macro: cTr

Description: définit le Th du tableau.

Parameters:
@param - id - string - optional - l'ID du Th du tableau
@param - class - string - optional - permet d'ajouter une classe CSS au Th du tableau
@param - scope - string - optional - permet de définir le scope de la cellule (par défaut: 'col', valeurs possibles: 'col', 'row') 
@param - role - string - optional - permet de définir le role du Th du tableau (par défaut: 'columnheader', valeurs possibles: 'columnheader', 'rowheader') 
@param - params - string - optional - permet d'ajouter des parametres HTML à la ligne du tableau
-->
<#macro cTh id='' class='' scope='col' role='columnheader' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<th scope="${scope!}" class="<#if class != ''>${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> >
    <#nested>
</th>
</#macro>