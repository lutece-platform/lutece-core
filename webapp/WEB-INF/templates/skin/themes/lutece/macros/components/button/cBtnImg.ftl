<#-- Macro: cBtnImg

Description: affiche un bouton avec une image.

Parameters:
@param - id - string - optional - l'ID du bouton
@param - class - string - optional - permet d'ajouter une classe CSS au bouton (par défaut: 'primary')
@param - src - string - required - permet de définir l'image dans le bouton
@param - type - string - optional - permet de modifier le type de bouton (par défaut: 'submit')
@param - imgPos - string - optional - permet de gérer la position de l'image (par défaut: 'before', valeurs possibles: 'before', 'after')
@param - disabled - boolean - optional - permet de désactiver le bouton (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML au bouton
 -->
<#macro cBtnImg src class='primary' id='' type='submit' params='' imgPos='before' disabled=false deprecated...>
<@deprecatedWarning args=deprecated />
<@cBtn label='' class='${class!} btn-img' type=submit  disabled=disabled params=params >
<#if imgPos='before'>
    <#if type='svg'>
    <@cFigure>${src}</@cFigure>
    <#else>
    <@cImg src="${src!}" alt='' />
    </#if>
</#if>
<@cText type='span'><#nested></@cText>
<#if imgPos='after'>
    <#if type='svg'>
        <@cFigure>${src}</@cFigure>
    <#else>
        <@cImg src="${src!}" title="${title!}" alt='' class='after' />
    </#if>
</#if>
</@cBtn>
</#macro>