<#-- Macro: cCardLayout

Description: affiche une grille de cartes.

Parameters:

@param - id - string - optional - l'ID de la grille de cartes
@param - class - string - optional - permet d'ajouter une classe CSS à la grille de cartes
@param - rowCols - string - optional - permet de spécifier le nombre de colonnes par ligne dans la grille de cartes
@param - type - string - optional - permet de définir le type de la grille de carte (par défaut: 'group', valeurs possibles: 'groupe', 'deck', 'columns')
@param - params - string - optional - permet d'ajouter des parametres HTML à la grille de cartes
-->
<#macro cCardLayout type='group' rowCols='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if type == 'group'>
    <#local cClass>row g-0</#local>
<#elseif type == 'deck'>
    <#local cClass>row g-4 row-cols-${rowCols!} ${class!}</#local>
<#elseif type == 'columns'>
    <#local cParams>data-masonry='{"percentPosition": true }' ${params!}</#local>
    <#local cClass>row ${class!}</#local>
</#if>
<@cBlock class=cClass id=id params=cParams>
<#nested>
</@cBlock>
<#if type == 'columns'>
    <script src="${commonsSiteThemePath}${commonsSiteJsPath}vendor/masonry/masonry.pkgd.min.js" charset="utf-8"></script>
</#if>
</#macro>